package ch.adrianelsener.train.pi.server;

import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Result;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class TcpTestClient {
    public static void main(String[] args) throws IOException {
        new TcpTestClient().send("foo");
    }

    private void send(final String message) throws IOException {
        sendReceive();
        sendReceive();
    }

    private void sendReceive() throws IOException {
        Socket socket = new Socket("127.0.0.1", 2323);
        DataOutputStream streamWriter = new DataOutputStream(socket.getOutputStream());
        Command testDto = new Command();
        testDto.setData("gugus");
        String dtoAsJsonString = new Gson().toJson(testDto);
        PrintWriter writer = new PrintWriter(streamWriter);
        writer.println(dtoAsJsonString);
        writer.flush();
        System.out.printf("sent wait for response\n");
        InputStream inputStream = socket.getInputStream();

        BufferedReader inputReader = new BufferedReader(new InputStreamReader(new DataInputStream(inputStream)));
        String line = inputReader.readLine();
        inputReader.close();
        streamWriter.close();
        socket.close();
        System.out.printf("string : %s\n", line);
        Result receivedDto = new Gson().fromJson(line, Result.class);

        System.out.printf("client received from : {%s}\n", receivedDto);
    }
}
