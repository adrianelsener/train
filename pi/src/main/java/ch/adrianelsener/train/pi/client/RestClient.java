package ch.adrianelsener.train.pi.client;

import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Result;
import ch.adrianelsener.train.pi.tcp.TcpClient;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import javax.ws.rs.core.MediaType;

public class RestClient implements TcpClient {
    @Override
    public Result sendCommand(final Command cmd) {
        ClientRequest request = new ClientRequest("http://localhost:8080/train/api/info");
        request.accept(MediaType.APPLICATION_JSON_TYPE);
        try {
            final ClientResponse<String> response = request.get(String.class);
            response.getStatus();
            response.getEntity(String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
