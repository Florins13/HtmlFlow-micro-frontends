package com.dev;

import com.dev.dto.OrderDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

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
}
