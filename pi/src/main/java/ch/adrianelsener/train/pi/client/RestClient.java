package ch.adrianelsener.train.pi.client;

import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Result;
import ch.adrianelsener.train.pi.server.GsonMessageBodyHandler;
import ch.adrianelsener.train.pi.tcp.TcpClient;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestClient implements TcpClient {
    @Override
    public Result sendCommand(final Command cmd) {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://127.0.0.1:8080/train/api/speed");
        client.register(GsonMessageBodyHandler.class);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        Result value = response.readEntity(Result.class);
        response.close();
        return value;
    }
}
