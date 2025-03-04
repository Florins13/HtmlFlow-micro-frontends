package htmlflow;

import htmlflow.visitor.HtmlViewVisitor;
import org.xmlet.htmlapifaster.Html;

public class HtmlMfe extends HtmlPage{
//    private final HtmlTemplate template;
    private final String identifier;
    private final HtmlViewVisitor visitor;
    private final String customTagName;


    public HtmlMfe(String identifier, HtmlViewVisitor visitor, String customTagName){
        this.identifier = identifier;
        this.visitor = visitor;
        this.customTagName = customTagName;
    };


    public HtmlViewVisitor getVisitor() {
        return visitor;
    };

    @Override
    public Html<HtmlPage> html() {
        getVisitor().write(HEADER);
        return new Html<>(this);
    }

    @Override
    public HtmlPage setIndented(boolean isIndented) {
        return null;
    }

    @Override
    public HtmlPage threadSafe() {
        return null;
    }

//    @Override
//    public ElementVisitor getVisitor() {
//        return null;
//    }

    @Override
    public String getName() {
        return "HtmlMfe";
    }

    public String render() {
        StringBuilder str = ((StringBuilder) getVisitor().out());
        str.setLength(0);
        getVisitor().resolve(null);
        return str.toString();
    }

//    public String render() {
//        // Clear any old content
//        StringBuilder sb = (StringBuilder) visitor.out();
//        sb.setLength(0);
//
//        // Start the skeleton
//        html()
//                .head()
//                .title().text("Single MFE Component").__()
//                .__()          // </head>
//                .body().div().text("my custom tag").__()
//                // Insert your single custom tag with an ID
//                .raw("<" + customTagName + " id=\"" + identifier + "\"></" + customTagName + ">")
//                .__()          // </body>
//                .__();             // </html>
//
//        return sb.toString();
//    }
}
