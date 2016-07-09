package ch.adrianelsener.train.pi.server;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@Path("info")
public class InfoTextService {

    @GET
    @Produces("text/plain")
    public String getInfoText() {
        final StringWriter sw = new StringWriter();
        final PrintWriter infoWriter = new PrintWriter(sw);
        try {
            // display a few of the available system information properties
            infoWriter.println("----------------------------------------------------");
            infoWriter.println("HARDWARE INFO");
            infoWriter.println("----------------------------------------------------");
            return sw.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }
}
