package com.dev;

import htmlflow.HtmlDoc;
import htmlflow.HtmlFlow;
import htmlflow.HtmlMfe;
import htmlflow.HtmlView;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.xmlet.htmlapifaster.EnumTypeScriptType;
import org.xmlet.htmlapifaster.S;

import java.io.StringWriter;

import static java.lang.System.lineSeparator;

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
                    .mfe("html-segment", "http://localhost:8080/resource_alpha").addAttr("test", "red").__()
                    .div().mfe("html-segment", "http://localhost:8080/resource_beta").__()
                    .div()
                    .custom("html-segment").addAttr("url", "http://localhost:3000/resource_omega")
                    .__()
                    .__()
                    .__() // body
                    .__(); // html
        });

//        StringWriter writer = new StringWriter();
//        HtmlDoc htmlDoc = HtmlFlow.doc(writer);
//        htmlDoc.html()
//                    .head()
//                    // Reference JS file in META-INF/resources/mfe-script.js
//                    .script().attrType(EnumTypeScriptType.MODULE).attrSrc("/mfe-script.js").__()
//                    .__() // head
//                    .body().h1().text("Hello from MFE!")
//                    .__()
//                    .div()
//                    .mfe("html-segment", "http://localhost:8080/resource_alpha").__()
//                    .div().mfe("html-segment", "http://localhost:8080/resource_beta").__()
//                    .div()
//                    .custom("html-segment").addAttr("url", "http://localhost:3000/resource_omega")
//                    .__()
//                    .__()
//                    .__() // body
//                    .__(); // html


//        HtmlView<?> view = HtmlFlow.view(page -> page.html().body()
//                .div()
//                .mfe("some-comment", "testView")
//                .__() // script
//                .__());


//        String html = writer.toString();
        String html = mfe.render();
        return Response.ok(html).build();

    }
}
