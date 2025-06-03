package com.dev;

import com.dev.dto.OrderDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey = "order-client")
@Path("/order/history")
public interface OrderClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<OrderDTO> getOrderHistory();

}
