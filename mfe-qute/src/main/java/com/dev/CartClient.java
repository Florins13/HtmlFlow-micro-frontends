package com.dev;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "http://localhost:8080") // your remote base URL
@Path("/cart")
public interface CartClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    CartDTO getCart();

    @POST
    @Path("/add/{id}")
    CartDTO addToCart(@PathParam("id") Long id);
}
