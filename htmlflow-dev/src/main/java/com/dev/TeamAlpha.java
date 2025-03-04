package com.dev;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import htmlflow.HtmlDoc;
import htmlflow.HtmlFlow;

import java.io.StringWriter;

@Path("/resource_alpha")
public class TeamAlpha {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getHtml() {
        StringWriter writer = new StringWriter();
        HtmlDoc htmlDoc = HtmlFlow.doc(writer);
        htmlDoc.html()
                .head()
                .title().text("Quarkus HTML Demo").__()
                .__() // close head
                .body()
                .p().text("Hello from team ALPHA").__()
                .__(); // close html
        return Response.ok(writer.toString()).build();
    }
}
