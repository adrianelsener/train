package ch.adrianelsener.train.pi.server;


import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("speed")
public class SpeedService {
    private static Logger logger = LoggerFactory.getLogger(SpeedService.class);
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInfoText(Command dto) {
        logger.debug("received {}", dto);
        final Result result = dto.execute();
        logger.debug("send {}", result);
        return Response.ok(result).build();
    }


}
