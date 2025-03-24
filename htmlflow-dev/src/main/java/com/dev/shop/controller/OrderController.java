package com.dev.shop.controller;


import com.dev.shop.controller.dto.OrderDTO;
import com.dev.shop.model.Order;
import com.dev.shop.service.OrderService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/order")
public class OrderController {
    @Inject
    OrderService orderServiceImpl;


    @POST
    @Path("/finalise")
//    @RolesAllowed("BASIC")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response order(@FormParam("fullName") String fullName,
                                  @FormParam("address") String address,
                                  @FormParam("telephone") Integer telephone,
                                  @FormParam("zipCode") String zipCode,
                                  @FormParam("acquire") String acquireType) throws Exception {
        if (orderServiceImpl.getUserCart().cartIsEmpty())
            throw new Exception("Your cart is empty, please add items and try again!");
        OrderDTO newOrder = new OrderDTO(orderServiceImpl.placeOrder(fullName, address, telephone, zipCode, acquireType));
        return Response.ok(newOrder).build();
    }

    @GET
    @Path("/history")
//    @RolesAllowed("BASIC")
    @Produces(MediaType.TEXT_HTML)
    public Response getOrders() {
        List<OrderDTO> orderDTOList = orderServiceImpl.getOrders().stream().map(order -> new OrderDTO(order)).collect(Collectors.toList());
        return Response.ok(orderDTOList).build();
    }
}
