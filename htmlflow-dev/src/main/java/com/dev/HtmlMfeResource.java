package com.dev;


import htmlflow.HtmlFlow;
import htmlflow.HtmlMfe;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.xmlet.htmlapifaster.EnumTypeScriptType;

@Path("/mfe")
public class HtmlMfeResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getHtml() {
        HtmlMfe mfe = HtmlFlow.mfe(page -> {
            page.html()
                    .head()
                    // Reference JS file in META-INF/resources/mfe-script.js
                        .script().attrType(EnumTypeScriptType.MODULE).attrSrc("/mfe-script.js").__()
                    .__()
                        .body()
                        .div().addAttr("style", "display: flex; justify-content: center;height: 100px;border: blue 1px solid;")
                            .h1().text("Header -> Hello from HtmlFlow!").__()
                        .__()
                        .div().addAttr("style", "display: flex;")
                            .div().addAttr("style", "height:800px; width: 75%;border: black 1px solid; margin: 20px").mfe("team-black", "http://localhost:4200/").__()
                            .div().addAttr("style", "height:800px; width: 20%;border: red 1px solid; margin: 20px").mfe("team-red", "http://localhost:8080/cart").__()
                        .__()
                    .div()
                        .div().addAttr("style", "border: green 1px solid; margin: 20px").mfe("team-green", "http://localhost:8080/order/history").__()
                    .__()
                    .div().addAttr("style", "display: flex; justify-content: center;height: 150px;border: yellow 1px solid;")
                        .div().mfe("team-yellow", "http://localhost:8080/html-chunked/stream").__()
                    .__()
                    .__();
        });

        String html = mfe.render();
        return Response.ok(html).build();

    }
}
