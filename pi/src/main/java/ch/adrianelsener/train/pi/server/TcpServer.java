package ch.adrianelsener.train.pi.server;

import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Result;
import com.google.gson.Gson;

import java.io.*;
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
            try {
                System.out.printf("started wait for connect\n");
                Socket socket = serverSocket.accept();
                System.out.printf("connected\n");

                Command obj = readJson(socket);
                System.out.printf("%s\n", obj);
                Result result = obj.execute();
                System.out.printf("%s\n", result);
                PrintWriter streamWriter = new PrintWriter(new DataOutputStream(socket.getOutputStream()));
                streamWriter.println(new Gson().toJson(result));
                streamWriter.flush();
                streamWriter.close();
                socket.close();
            } catch (IOException ioex) {
                throw new IllegalStateException(ioex);
            }
        }
    }

    static Command readJson(final Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new DataInputStream(socket.getInputStream())));
        String line = in.readLine();
        System.out.printf("read line: %s", line);
        return new Gson().fromJson(line, Command.class);
    }


    String leseNachricht(Socket socket) throws IOException {
        BufferedReader bufferedReader =
                new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));
        char[] buffer = new char[200];
        int anzahlZeichen = bufferedReader.read(buffer, 0, 200); // blockiert bis Nachricht empfangen
        String nachricht = new String(buffer, 0, anzahlZeichen);
        return nachricht;
    }
}
