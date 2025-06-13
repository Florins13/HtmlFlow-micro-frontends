package htmlflow.visitor;

import htmlflow.continuations.HtmlContinuation;
import htmlflow.continuations.HtmlContinuationSyncStatic;
import org.xmlet.htmlapifaster.MfeConfiguration;

import java.lang.reflect.Field;

import static htmlflow.visitor.PreprocessingVisitor.HtmlContinuationSetter.setNext;

public class PreprocessingVisitorMfe extends PreprocessingVisitor{
    public PreprocessingVisitorMfe(boolean isIndented) {
        super(isIndented);
    }

    //reflection try
//    @Override
//    public void resolve(Object model) {
//        super.resolve(model);
//        System.out.println(this.staticBlockIndex);
//
//        StringBuilder scriptTags = new StringBuilder();
//        for (MfeConfiguration mfeConfig : this.getMfePage()) {
//            String scriptSrc = mfeConfig.getMfeScriptUrl();
//            if (scriptSrc != null && !scriptSrc.isEmpty()) {
//                scriptTags.append("<script type=\"module\" src=\"")
//                        .append(scriptSrc)
//                        .append("\"></script>");
//            }
//        }
//
//        HtmlContinuation curr = this.first;
//        while (curr != null && curr.next != null){
//            if(curr instanceof HtmlContinuationSyncStatic){
//                String content = ((HtmlContinuationSyncStatic) curr).staticHtmlBlock;
//
//                if(content.contains("</head>")){
//                    Field staticHtmlBlockField = null;
//                    try {
//                        staticHtmlBlockField = HtmlContinuationSyncStatic.class.getDeclaredField("staticHtmlBlock");
//                        staticHtmlBlockField.setAccessible(true);
//                        String currentHtml = ((HtmlContinuationSyncStatic) curr).staticHtmlBlock;
//                        String newHtml = currentHtml.replaceFirst("</head>", scriptTags + "</head>");
//                        staticHtmlBlockField.set(curr, newHtml.intern());
//                        break;
//                    } catch (NoSuchFieldException | IllegalAccessException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//
//            }
//            curr = curr.next; // Move to the next node in the linked list
//        }
    @Override
    public void resolve(Object model) {
        // Build script tags
        StringBuilder scriptTags = new StringBuilder();
        scriptTags.append("<script type=\"module\" src=\"base.js\"></script>");
        for (MfeConfiguration mfeConfig : this.getMfePage()) {
            String scriptSrc = mfeConfig.getMfeScriptUrl();
            if (scriptSrc != null && !scriptSrc.isEmpty()) {
                scriptTags.append("<script type=\"module\" src=\"")
                        .append(scriptSrc)
                        .append("\"></script>");
            }
        }

        // Don't call super.resolve() which creates a final static block

        // Process the static HTML directly
        String staticHtml = sb().substring(staticBlockIndex);


        // Inject scripts if needed
        if (staticHtml.contains("</head>")) {
            staticHtml = staticHtml.replaceFirst("</head>", scriptTags + "</head>");
        }

        HtmlContinuation staticCont = new HtmlContinuationSyncStatic(staticHtml.trim(), this, null);
        last = first == null
                ? first = staticCont         // assign both first and last
                : setNext(last, staticCont); // append new staticCont and return it to be the new last continuation.
    }

}
