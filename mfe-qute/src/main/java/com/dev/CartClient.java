package com.dev;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "http://api:8080") // your remote base URL
@Path("/cart")
public interface CartClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    CartDTO getCart();

}
