package htmlflow;

import htmlflow.visitor.HtmlMfeVisitor;
import org.xmlet.htmlapifaster.Html;
import org.xmlet.htmlapifaster.MfeConfiguration;


public class HtmlMfe extends HtmlPage{
    private final HtmlMfeVisitor visitor;

    public HtmlMfe(HtmlMfeVisitor visitor){
        this.visitor = visitor;
    }


    public HtmlMfeVisitor getVisitor() {
        return visitor;
    };

    @Override
    public Html<HtmlPage> html() {
        getVisitor().write(HEADER);
        return new Html<>(this);
    }

    @Override
    public HtmlPage setIndented(boolean isIndented) {
        return new HtmlMfe(visitor.clone(isIndented));
    }

    @Override
    public HtmlPage threadSafe() {
        return null;
    }

    @Override
    public String getName() {
        return "HtmlMfe";
    }

    public String render() {
        StringBuilder str = (StringBuilder) visitor.out();
        getVisitor().resolve(null);

        // Get the generated HTML
        String html = str.toString();

        // Build script tags
        StringBuilder scriptTags = new StringBuilder();

        // Add base script
        scriptTags.append("<script type=\"module\" src=\"base.js\"></script>");

        // Add script tags for each MFE configuration
        for (MfeConfiguration mfeConfig : this.getVisitor().getMfePage()) {
            String scriptSrc = mfeConfig.getMfeScriptUrl();
            if (scriptSrc != null && !scriptSrc.isEmpty()) {
                scriptTags.append("<script type=\"module\" src=\"")
                        .append(scriptSrc)
                        .append("\"></script>");
            }

            // interestingly it loads faster the css if they are also added in the head.
//            String cssSrc = mfeConfig.getMfeStylingUrl();
//            if (cssSrc != null && !cssSrc.isEmpty()) {
//                scriptTags.append("<link rel=\"stylesheet\" href=\"")
//                        .append(cssSrc)
//                        .append("\">");
//            }

        }

        // Insert script tags before the closing </head> tag
        return html.replaceFirst("</head>", scriptTags.toString() + "</head>");

    }
}
