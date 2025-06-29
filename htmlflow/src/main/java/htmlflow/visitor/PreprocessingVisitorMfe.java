package htmlflow.visitor;

import htmlflow.continuations.HtmlContinuation;
import htmlflow.continuations.HtmlContinuationSyncStatic;
import org.xmlet.htmlapifaster.MfeConfiguration;

import java.lang.reflect.Field;


public class PreprocessingVisitorMfe extends PreprocessingVisitor{
    public PreprocessingVisitorMfe(boolean isIndented) {
        super(isIndented);
    }

    @Override
    public void resolve(Object model) {
        super.resolve(model);

        StringBuilder scriptTags = new StringBuilder();
        scriptTags.append("<script type=\"module\" src=\"base.js\"></script>");
        scriptTags.append("<script type=\"module\" src=\"http://localhost:8083/index-cart.js\"></script>");
        scriptTags.append("<link rel=\"stylesheet\" src=\"http://localhost:8083/index-css.css\"></link>");
        for (MfeConfiguration mfeConfig : this.getMfePage()) {
            String scriptSrc = mfeConfig.getMfeScriptUrl();
            if (scriptSrc != null && !scriptSrc.isEmpty()) {
                scriptTags.append("<script type=\"module\" src=\"")
                        .append(scriptSrc)
                        .append("\"></script>");
            }
        }

        HtmlContinuation curr = this.first;
        while (curr != null) {
            if (curr instanceof HtmlContinuationSyncStatic) {
                String content = ((HtmlContinuationSyncStatic) curr).staticHtmlBlock;

                if (content.contains("</head>")) {
                    Field staticHtmlBlockField;
                    try {
                        staticHtmlBlockField = HtmlContinuationSyncStatic.class.getDeclaredField("staticHtmlBlock");
                        staticHtmlBlockField.setAccessible(true);
                        String newHtml = content.replaceFirst("</head>", scriptTags + "</head>");
                        staticHtmlBlockField.set(curr, newHtml.intern());
                        break;
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
            curr = curr.next; // Move to the next node in the linked list
        }
    }

}
