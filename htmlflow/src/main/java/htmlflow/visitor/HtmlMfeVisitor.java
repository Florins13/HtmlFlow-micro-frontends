package htmlflow.visitor;


import htmlflow.continuations.HtmlContinuation;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.SuspendConsumer;
import org.xmlet.htmlapifaster.async.AwaitConsumer;

import java.util.function.BiConsumer;

public class HtmlMfeVisitor extends HtmlVisitor{

    public final HtmlContinuation first;

    public HtmlMfeVisitor(Appendable out, boolean isIndented, HtmlContinuation first) {
        super(out, isIndented);
        this.first = first.copy(this);
    }
    /**
     * Processing output through invocation of HtmlContinuation objects chain.
     */
    @Override
    public final void resolve(Object model) {
        first.execute(model);
    }

    @Override
    public final <E extends Element, U> void visitDynamic(E element, BiConsumer<E, U> dynamicHtmlBlock) {
        throw new IllegalStateException("Wrong use of dynamic() in a static view! Use HtmlView to produce a dynamic view.");
    }

//    @Override
//    public <E extends Element> void visitMfe(E element, String segmentName, String resource) {
//        // here wht is my element null?
//        element.custom(segmentName).addAttr("url", resource);
//    }

    @Override
    public final <M, E extends Element> void visitAwait(E element, AwaitConsumer<E,M> asyncAction) {
        throw new IllegalStateException("Wrong use of async() in a static view! Use HtmlViewAsync to produce an async view.");
    }

    @Override
    public <M, E extends Element> void visitSuspending(E element, SuspendConsumer<E, M> suspendAction) {
        throw new IllegalStateException("Wrong use of suspending() in a static view! Use HtmlViewSuspend to produce an async view.");
    }

    @Override
    public final HtmlMfeVisitor clone(boolean isIndented)  {
        return new HtmlMfeVisitor(out, isIndented, first);
    }

}
