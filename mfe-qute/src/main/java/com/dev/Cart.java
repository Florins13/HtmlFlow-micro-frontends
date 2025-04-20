package com.dev;

import io.quarkus.qute.Template;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/cart")
public class Cart {

    @Inject
    Template cart;

    @Inject
    @RestClient
    CartClient cartClient;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getCart() {
        CartDTO cartDTO = cartClient.getCart();
        return Response.ok(cart.data("cart", cartDTO).render()).build();
    };


    @POST
    @Path("/add/{id}")
    @Produces(MediaType.TEXT_HTML)
    public Response addItem(@PathParam("id") Long id) {
        CartDTO cartDTO = cartClient.addToCart(id);
        return Response.ok(cart.data("cart", cartDTO).render()).build();
    }
}
