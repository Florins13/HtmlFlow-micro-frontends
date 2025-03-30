package com.dev.shop.controller;


import com.dev.shop.controller.dto.CartDTO;
import com.dev.shop.controller.dto.CheckoutDTO;
import com.dev.shop.service.CartService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/cart")
public class CartController {

    @Inject
    CartService cartServiceImpl;

    @Inject
    Template cart;


    @GET
//    @RolesAllowed("BASIC")
    @Produces(MediaType.TEXT_HTML)
    public Response getCart() {
//        CartDTO newCart = new CartDTO(cartServiceImpl.getUserCart());
        return Response.ok(cart.data("cart", new CartDTO(cartServiceImpl.getUserCart())).render()).build();
    }

    @POST
    @Path("/add/{id}")
//    @RolesAllowed("BASIC")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCarts(@PathParam("id") Long id) {
        cartServiceImpl.addToCart(id);
        return Response.ok().build();
    }


    @POST
    @Path("/deleteItem")
//    @RolesAllowed("BASIC")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response deleteCartItem(@FormParam("id") Long id) {
        cartServiceImpl.deleteCartItem(id);
        return null;
    }


    @GET
    @Path("/checkout")
//    @RolesAllowed("BASIC")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkout() {
        if (cartServiceImpl.getUserCart().cartIsEmpty()) return getCart();
        CheckoutDTO checkoutDTO = new CheckoutDTO(cartServiceImpl.getUserCart(), cartServiceImpl.calculateRentTotal());
        return Response.ok(checkoutDTO).build();
    }


}
