package com.dev;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(baseUri = "http://localhost:8080") // your remote base URL
@Path("/order/history")
public interface OrderClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<OrderDTO> getOrderHistory();

}
