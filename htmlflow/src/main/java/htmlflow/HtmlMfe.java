package htmlflow;

import htmlflow.visitor.HtmlVisitor;
import org.xmlet.htmlapifaster.Html;

import java.util.List;


public class HtmlMfe extends HtmlPage{
    private final HtmlVisitor visitor;
    private final List<HtmlMfeConfig> mfeConfigList;
//    private final HtmlTemplate template;

    public HtmlMfe(List<HtmlMfeConfig> mfeConfigList, HtmlVisitor visitor){
        this.visitor = visitor;
        this.mfeConfigList = mfeConfigList;
//        this.template = template;
    }


    public HtmlVisitor getVisitor() {
        return visitor;
    };

    @Override
    public Html<HtmlPage> html() {
        getVisitor().write(HEADER);
        // we could set the script from microservice origin
        // getVisitor().write("<script src='/test.js' type='module'></script>");
        return new Html<>(this);
    }

    @Override
    public HtmlPage setIndented(boolean isIndented) {
        return new HtmlMfe(this.mfeConfigList, visitor.clone(isIndented));
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
