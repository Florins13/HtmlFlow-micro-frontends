package com.dev;

import htmlflow.HtmlFlow;
import htmlflow.HtmlMfe;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.xmlet.htmlapifaster.EnumTypeScriptType;

import java.io.StringWriter;

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
                    .__() // head
                    .body().h1().text("Hello from MFE!")
                    .__()
                    .div()
                    .custom("html-segment").addAttr("url", "http://localhost:8080/resource_alpha")
                    .__()
                    .div()
                    .custom("html-segment").addAttr("url", "http://localhost:8080/resource_beta")
                    .__()
                    .div()
                    .custom("html-segment").addAttr("url", "http://localhost:3000/resource_omega")
                    .__()
                    .__() // body
                    .__(); // html
        }, "tag");

        String html = mfe.render();
        return Response.ok(html).build();
    }
}
