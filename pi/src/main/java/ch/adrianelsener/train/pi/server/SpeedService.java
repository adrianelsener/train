package ch.adrianelsener.train.pi.server;


import ch.adrianelsener.train.pi.dto.AccelerationDto;
import ch.adrianelsener.train.pi.dto.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("speed")
public class SpeedService {
    private static Logger logger = LoggerFactory.getLogger(SpeedService.class);
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInfoText(AccelerationDto dto) {
        logger.debug("received {}", dto);
        final Result ok = Result.ok(new AccelerationDto().setSpeed(12));
        return Response.ok(ok).build();
    }


}
