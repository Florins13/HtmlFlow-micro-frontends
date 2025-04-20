package com.dev;

import com.dev.dto.OrderDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(baseUri = "http://localhost:8080") // your remote base URL
@Path("/order")
public interface OrderClient {

    @GET
    @Path("/history")
    @Produces(MediaType.APPLICATION_JSON)
    List<OrderDTO> getOrderHistory();

    @POST
    @Path("/finalise")
    @Produces(MediaType.APPLICATION_JSON)
    OrderDTO finaliseTransaction();
}
