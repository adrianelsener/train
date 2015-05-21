package ch.adrianelsener.train.pi.server;

import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Result;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by els on 15.05.15.
 */
public class TcpServer {
    final int listenPort = 2323;

    public static void main(String[] args) throws IOException {
        new TcpServer().initConnection(2323);
    }

    private TcpServer() {

    }

    private void initConnection(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        new Thread(new ServerRunner(serverSocket)).start();
    }

    private static class ServerRunner implements Runnable {
        private final ServerSocket serverSocket;

        ServerRunner(ServerSocket socket) {
            this.serverSocket = socket;
        }

        @Override
        public void run() {
            while (true) {
                resend();
            }
        }

        private void resend() {
            System.out.printf("started wait for connect\n");
            try (Socket socket = serverSocket.accept()) {
                System.out.printf("connected\n");
                Command obj = readJson(socket);
                System.out.printf("%s\n", obj);
                Result result = obj.execute();
                System.out.printf("%s\n", result);
                send(socket, result);
            } catch (IOException ioex) {
                throw new IllegalStateException(ioex);
            }
        }
    }

    private static void send(final Socket socket, final Object data) throws IOException {
        PrintWriter streamWriter = new PrintWriter(new DataOutputStream(socket.getOutputStream()));
        streamWriter.println(new Gson().toJson(data));
        streamWriter.flush();
    }

    static Command readJson(final Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new DataInputStream(socket.getInputStream())));
        String line = in.readLine();
        System.out.printf("read line: %s", line);
        return new Gson().fromJson(line, Command.class);
    }
}
