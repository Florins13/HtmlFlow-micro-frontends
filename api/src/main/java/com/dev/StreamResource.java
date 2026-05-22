package com.dev;

import io.reactivex.rxjava3.core.Observable;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Path("/html-chunked")
public class StreamResource {

    private static final long ROW_DELAY_MS = 1000;

    // Simulated order history data: transaction, status, item, quantity, total
    private static final String[][] ORDER_ROWS = {
            {"a3f1-b8c2", "COMPLETED",  "Mountain Pro X1",  "1", "1299.99"},
            {"d7e4-f5a9", "SHIPPING",   "Urban Cruiser S3", "2", "1598.00"},
            {"c2b8-e1d6", "PROCESSING", "Road Racer R7",    "1", "2450.00"},
            {"f9a3-d4c7", "COMPLETED",  "Trail Blazer T5",  "3", "2697.00"},
            {"b5e1-a8f2", "WAITING_PAYMENT", "City Commuter C2", "1", "899.99"}
    };

    static final Observable<String[]> orderRowStream = Observable
            .fromArray(ORDER_ROWS)
            .concatMap(row -> Observable.just(row).delay(ROW_DELAY_MS, TimeUnit.MILLISECONDS));

    public static void streamOrderHistory(Writer writer) throws IOException {
        // First chunk: table with header
        writer.write("<table data-stream=\"host\">"
                + "<thead><tr>"
                + "<th>Transaction</th>"
                + "<th>Status</th>"
                + "<th>Item</th>"
                + "<th>Qty</th>"
                + "<th>Total (€)</th>"
                + "</tr></thead>"
                + "<tbody data-stream=\"order\">");
        writer.write("</tbody></table>");
        writer.flush();

        // Each row arrives as a separate chunk with a delay
        orderRowStream.blockingForEach(row -> {
            writer.write("<tr data-stream=\"order\""
                    + "<td>" + row[0] + "</td>"
                    + "<td>" + row[1] + "</td>"
                    + "<td>" + row[2] + "</td>"
                    + "<td>" + row[3] + "</td>"
                    + "<td>" + row[4] + "</td>"
                    + "</tr>");
            writer.flush();
        });

        // Final chunk: close the table

        writer.flush();
        writer.close();
    }

    @Path("/writer")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Reader getHtml() throws IOException {
        return buildReaderFromWriterBlock(writer -> {
            try {
                streamOrderHistory(writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Path("/stream")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response streamResponse() {
        StreamingOutput stream = (OutputStream output) -> {
            streamOrderHistory(new OutputStreamWriter(output));
        };
        return Response.ok(stream).build();
    }

    private static Reader buildReaderFromWriterBlock(Consumer<Writer> block) throws IOException {
        PipedWriter writer = new PipedWriter();
        PipedReader reader = new PipedReader(writer);
        Thread writerThread = new Thread(() -> {
            block.accept(writer);
        });
        writerThread.start();
        return reader;
    }
}
