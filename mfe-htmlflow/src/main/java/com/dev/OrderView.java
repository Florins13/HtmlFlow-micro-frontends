package com.dev;

import com.dev.dto.OrderDTO;
import com.dev.dto.ShippingItemDTO;
import htmlflow.HtmlFlow;
import htmlflow.HtmlView;

import java.util.List;

public class OrderView {
    public static final HtmlView<List<OrderDTO>> orderHistoryView = HtmlFlow.view(content -> {
        content
                .div()
                .attrStyle("padding-left: 10px")
                .h3().text("Order history:").__()
                .table().attrStyle("width: 100%")
                .tr()
                .th().text("Transaction").__()
                .th().text("Order state").__()
                .th().text("Acquire Type").__()
                .th().text("Items:").__()
                .th().text("Total").__()
                .th().text("Address").__()
                .th().text("User").__()
                .__()
                .<List<OrderDTO>>dynamic( (table, orders) -> {
                    for (OrderDTO order : orders) {
                        table.tr()
                                .td().text(order.getTransaction()).__()
                                .td().text(order.getOrderState()).__()
                                .td().text(order.getAcquireType()).__()
                                .td().attrStyle("display: flex;flex-direction: column;")
                                .of(tdCol -> {
                                    for (ShippingItemDTO item : order.getShippingItems()) {
                                        tdCol.span().text(item.getQuantity() + "x " + item.getBike().getModel()).__();
                                    }
                                })
                                .__()
                                .td().text(String.valueOf(order.getTotalPrice())).__()
                                .td()
                                .span().text(
                                        order.getShippingAddress().getFullName() + ", "
                                                + order.getShippingAddress().getAddress() + ", "
                                                + order.getShippingAddress().getTelephone() + ". "
                                                + order.getShippingAddress().getZipCode()
                                ).__()
                                .__()
                                .td().text(order.getUsername()).__()
                                .__();
                    }
                })
                .__()
                .__();
    });
}
