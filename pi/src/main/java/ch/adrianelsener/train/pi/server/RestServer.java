package ch.adrianelsener.train.pi.server;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class RestServer {
    private static final String BASE_URI = "http://0.0.0.0:8080/train/api";

    private HttpServer httpServer;

    public void start() {
        final ResourceConfig rc = new ResourceConfig().packages(InfoTextService.class.getPackage().toString());
        httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public void stop() {
        httpServer.shutdown();
    }

}
