package com.dev;



import htmlflow.HtmlFlow;
import htmlflow.HtmlMfe;
import htmlflow.HtmlMfeConfig;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.xmlet.htmlapifaster.EnumTypeScriptType;

import java.util.ArrayList;
import java.util.List;

@Path("/mfe")
public class HtmlMfeResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getHtml() {
        // 1. should we have an initiator of the js files?
        // 2. HtmlMfeConfig is used in pot as well, to discuss the next point
        // 3. we could basically here set the script to the origin of the microservice, for now fetch from same origin, in this case 8082/mfe
        // 4. we could fetch css from the microservice or have the files in the resources of MFE
        List<HtmlMfeConfig> mfeConfigList = new ArrayList<>();

        HtmlMfeConfig mfeBike = new HtmlMfeConfig("http://localhost:8081/bikes", "micro-frontend", "triggerBikeEvent", "triggerCartEvent", "http://localhost:8081/js/mfe-spring.js", "");
        HtmlMfeConfig mfeCart = new HtmlMfeConfig("http://localhost:8083/cart", "team-red", "triggerCartEvent", "triggerOrderEvent", "http://localhost:8083/mfe-cart.js", "");
        HtmlMfeConfig mfeOrder = new HtmlMfeConfig("http://localhost:8084/order/history", "team-green", "triggerOrderEvent", "triggerBikeEvent", "", "");
        HtmlMfeConfig mfeStream = new HtmlMfeConfig("http://localhost:8080/html-chunked/stream", "team-yellow", "test", "test", "", "");
        mfeConfigList.add(mfeBike);
        mfeConfigList.add(mfeCart);
        mfeConfigList.add(mfeOrder);
        mfeConfigList.add(mfeStream);

        HtmlMfe mfe = HtmlFlow.mfe(mfeConfigList , page -> {
            page.html()
                    .head()
                    // Reference JS file in META-INF/resources/main.js
                        .script().attrType(EnumTypeScriptType.MODULE).attrSrc("main.js").__()
                    .__()
                        .body()
                        .div().addAttr("style", "display: flex; justify-content: center;height: 100px;border: blue 1px solid;")
                            .h1().text("Header -> Hello from HtmlFlow!").__()
                        .__()
                        .div().addAttr("style", "display: flex;")
                            .div().addAttr("style", "width: 75%;border: black 1px solid; margin: 20px")
                                .mfe(mfeBike).__()
//                                .script().attrType(EnumTypeScriptType.MODULE).attrSrc("http://localhost:8081/js/some-page.js").__()
                            .div().addAttr("style", "width: 20%;border: red 1px solid; margin: 20px")
                                .mfe(mfeCart).__()
                        .__()
                        .div()
                            .div().addAttr("style", "border: green 1px solid; margin: 20px")
                                .mfe(mfeOrder).__()
                        .__()
                    .div().addAttr("style", "display: flex; justify-content: center;height: 150px;border: yellow 1px solid;")
                        .div().mfe(mfeStream).__()
                    .__()
                    .__();
        });

        String html = mfe.render();
        return Response.ok(html).build();

    }
}
