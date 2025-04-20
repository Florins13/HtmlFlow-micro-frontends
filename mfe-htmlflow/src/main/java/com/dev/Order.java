package com.dev;

import com.dev.dto.OrderDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.List;

@Path("/order")
public class Order {

    @Inject
    @RestClient
    OrderClient orderClient;

    @GET
    @Path("/history")
    @Produces(MediaType.TEXT_HTML)
    public Response getOrders() {
        List<OrderDTO> orderDTOList = orderClient.getOrderHistory();
        String html = OrderView.orderHistoryView.render(orderDTOList);
        return Response.ok(html).build();
    }

    @POST
    @Path("/finalise")
    @Produces(MediaType.TEXT_HTML)
    public Response addItem() {
        List<OrderDTO> orderDTOList = new ArrayList<>();
        orderDTOList.add(orderClient.finaliseTransaction());
        String html = OrderView.orderHistoryView.render(orderDTOList);
        return Response.ok(html).build();
    }
}
