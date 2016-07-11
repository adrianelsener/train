package ch.adrianelsener.train.pi.server;

import ch.adrianelsener.train.pi.dto.AccelerationDto;
import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Mode;
import ch.adrianelsener.train.pi.dto.Result;
import com.google.gson.Gson;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpTestClient {





    public static void main(String[] args) throws IOException {
        new TcpTestClient().send("foo");
    }

    private void send(final String message) throws IOException {
        sendSetSpeed();
    }

    private Result sendSetSpeed() throws IOException {
        Gson gson = new Gson();
        Command cmd = Command.builder()//
                .setData(new AccelerationDto())//
                .setMode(Mode.SPEED)//
                .build();
        Result result = sendCommand(cmd);
        System.out.printf("%s\n", result);
        return result;
    }

    private Result sendCommand(final Command cmd) throws IOException {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://127.0.0.1:8080/train/api/speed");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        Result value = response.readEntity(Result.class);
        response.close();
        return value;
    }


    private Result sendCommandOld(final Command cmd) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8080);
        sendData(cmd, socket);
        Result receivedDto = receiveData(socket);
        socket.close();
        return receivedDto;
    }

    private Result receiveData(final Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(new DataInputStream(inputStream)));
        String line = inputReader.readLine();
        return new Gson().fromJson(line, Result.class);
    }

    private void sendData(final Command cmd, final Socket socket) throws IOException {
        DataOutputStream streamWriter = new DataOutputStream(socket.getOutputStream());
        String dtoAsJsonString = new Gson().toJson(cmd);
        PrintWriter writer = new PrintWriter(streamWriter);
        writer.println(dtoAsJsonString);
        writer.flush();
    }
}
