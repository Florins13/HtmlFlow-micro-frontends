package com.dev;

import com.dev.dto.OrderDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.net.URI;
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
    @Produces(MediaType.TEXT_HTML)
    @Path("/finalise")
    public Response addItem() {
        orderClient.finaliseTransaction();
        return Response.seeOther(URI.create("/order/history")).build();
    }
}
