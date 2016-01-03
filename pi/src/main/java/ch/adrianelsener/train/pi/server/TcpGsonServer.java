package ch.adrianelsener.train.pi.server;

import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Result;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpGsonServer {
    private static final Logger log = LoggerFactory.getLogger(TcpGsonServer.class);
    final int listenPort = 2323;

    public static void main(String[] args) throws IOException {
        new TcpGsonServer().initConnection(2323);
    }

    private TcpGsonServer() {
        super();
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
            log.info("started wait for connect");
            try (Socket socket = serverSocket.accept()) {
                log.info("connected");
                Command obj = readJson(socket);
                log.debug("cmd: {}", obj);
                Result result = obj.execute();
                log.debug("Result {}", result);
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
        log.info("read line: {}", line);
        Gson gson = new Gson();
        return gson.fromJson(line, Command.class);
    }
}
