package com.dev;

import io.quarkus.qute.Template;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
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
        cartDTO.getCartItems().forEach(cartItemDto -> {
            cartItemDto.getBike().setImageSource("http://localhost:8083/" + cartItemDto.getBike().getImageSource());
        });
        return Response.ok(cart.data("cart", cartDTO).render()).build();
    };
}
