package ch.adrianelsener.train.pi.server;


import ch.adrianelsener.train.pi.dto.AccelerationDto;
import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Result;
import com.google.gson.Gson;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.PrintWriter;
import java.io.StringWriter;

@Path("speed")
public class InfoText2Service {

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInfoText(AccelerationDto dto) {
        final Result ok = Result.ok(new AccelerationDto().setSpeed(12));
        return Response.ok(ok).build();
    }


}
