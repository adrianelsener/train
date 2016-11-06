package ch.adrianelsener.train.pi.dto.properties;

import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Result;
import ch.adrianelsener.train.pi.server.GsonMessageBodyHandler;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * This device is the holder/access path of the twi target
 */
public class TwiHolderDevice {
    public final static TwiHolderDevice NOT_INITIALIZED = new TwiHolderDevice((URL)null, Call.GSON_REST);
    public static final TwiHolderDevice LOCAL = NOT_INITIALIZED;
    private final URL url;
    private final Call callType;

    public TwiHolderDevice(URL url, Call callType) {
        this.url = url;
        this.callType = callType;
    }

    public TwiHolderDevice(String url, Call callType) {
        this(createUrl(url), callType);
    }

    private static URL createUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e){
            throw new IllegalArgumentException(e);
        }
    }

    public URL getUrl() {
        return url;
    }

    public Call getCallType() {
        return callType;
    }

    public enum Call {
        GSON_REST {
            public Result doCall(TwiHolderDevice holderDevice, Command cmd) {
                Client client = ClientBuilder.newBuilder().build();
                try {
                    WebTarget target = client.target(holderDevice.getUrl().toURI());
                    client.register(GsonMessageBodyHandler.class);
                    Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(cmd, MediaType.APPLICATION_JSON));
                    Result value = response.readEntity(Result.class);
                    response.close();
                    return value;
                } catch (URISyntaxException e) {
                    throw new IllegalArgumentException(e);
                }
//        WebTarget target = client.target("http://127.0.0.1:8080/train/api/speed");
            }
        };

        public abstract Result doCall(TwiHolderDevice holderDevice, Command cmd);
    }
}
