package ch.adrianelsener.train.pi.server;

import org.glassfish.grizzly.Grizzly;
import org.glassfish.grizzly.http.server.HttpServer;

import java.io.IOException;
import java.util.logging.Level;

public class RestServerStarter {
    public static void main(String[] args) {
        final RestServer restServer = new RestServer();
        Grizzly.logger(HttpServer.class).setLevel(Level.ALL);
        restServer.start();
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        restServer.stop();
    }

}