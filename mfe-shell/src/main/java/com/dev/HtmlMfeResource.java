package com.dev;



import htmlflow.HtmlFlow;
import htmlflow.HtmlView;
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
        HtmlView<?> mfe = HtmlFlow.ViewFactory.builder().mfeEnabled(true).build().view((page -> {
            page.html()
                    .head()
                    .script().attrType(EnumTypeScriptType.MODULE).attrSrc("https://cdn.jsdelivr.net/npm/@hotwired/turbo@latest/dist/turbo.es2017-esm.min.js").__()
                    .script().attrType(EnumTypeScriptType.MODULE).attrSrc("main.js").__()
//                    .meta().addAttr("name", "turbo-refresh-method").addAttr("content", "morph").__()
                    .__()
                        .body()
                        .div().addAttr("style", "display: flex; justify-content: center;height: 100px;border: blue 1px solid;")
                            .h1().text("Header -> Hello from HtmlFlow!").__()
                        .__()
                        .div().addAttr("style", "display: flex;")
                            .div().addAttr("style", "height:800px; width: 75%;border: black 1px solid; margin: 20px")
                                .custom("turbo-frame").addAttr("id", "test_bikes_frame").addAttr("src", "http://localhost:8081/bikes").addAttr("refresh", "morph").__()
//                                .script().attrType(EnumTypeScriptType.MODULE).attrSrc("http://localhost:8081/js/some-page.js").__()
                            .__()
                            .div().addAttr("style", "height:800px; width: 20%;border: red 1px solid; margin: 20px")
                                .custom("turbo-frame").addAttr("id", "test_cart_frame").addAttr("src", "http://localhost:8083/cart").__()
                            .__()
                        .__()
                        .div()
                            .div().addAttr("style", "border: green 1px solid; margin: 20px")
                                .custom("turbo-frame").addAttr("id", "test_transaction_frame").addAttr("src", "http://localhost:8084/order/history").__()
                        .__()
                    .__();
        }));

        String html = mfe.render();
        return Response.ok(html).build();

    }
}
