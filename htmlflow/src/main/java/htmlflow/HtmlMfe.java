package htmlflow;

import htmlflow.visitor.HtmlVisitor;
import org.xmlet.htmlapifaster.Html;



public class HtmlMfe extends HtmlPage{
    private final HtmlVisitor visitor;
//    private final HtmlTemplate template;

    public HtmlMfe(HtmlVisitor visitor){
        this.visitor = visitor;
//        this.template = template;
    }


    public HtmlVisitor getVisitor() {
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
        return str.toString();
    }
}
