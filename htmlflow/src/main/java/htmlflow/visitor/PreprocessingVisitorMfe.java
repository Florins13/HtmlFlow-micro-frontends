package htmlflow.visitor;

import htmlflow.continuations.HtmlContinuation;
import htmlflow.continuations.HtmlContinuationSyncStatic;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.SuspendConsumer;
import org.xmlet.htmlapifaster.async.AwaitConsumer;


import java.util.function.BiConsumer;

import static htmlflow.visitor.PreprocessingVisitor.HtmlContinuationSetter.setNext;

public class PreprocessingVisitorMfe extends HtmlMfeVisitor{
    /**
     * The internal String builder beginning index of a static HTML block.
     */
    protected int staticBlockIndex = 0;
    /**
     * The first node to be processed.
     */
    protected HtmlContinuation first;
    /**
     * The last HtmlContinuation
     */
    protected HtmlContinuation last;

    public PreprocessingVisitorMfe(boolean isIndented) {
        super(new StringBuilder(), isIndented);
    }

    /**
     * The main StringBuilder.
     */
    public final StringBuilder sb() {
        return (StringBuilder) out;
    }

    public final HtmlContinuation getFirst() {
        return first;
    }

    /**
     * Here we are creating 2 HtmlContinuation objects: one for previous static HTML and a next one
     * corresponding to the consumer passed to dynamic().
     * We will first create the dynamic continuation that will be the next node of the static continuation.
     *
     * U is the type of the model passed to the dynamic HTML block that is the same as T in this visitor.
     * Yet, since it came from HtmlApiFaster that is not typed by the Model, then we have to use
     * another generic argument for the type of the model.
     *
     * @param element The parent element.
     * @param dynamicHtmlBlock The continuation that consumes the element and a model.
     * @param <E> Type of the parent Element.
     * @param <U> Type of the model passed to the dynamic HTML block that is the same as T in this visitor.
     */
    @Override
    public <E extends Element, U> void visitDynamic(E element, BiConsumer<E, U> dynamicHtmlBlock) {
        throw new UnsupportedOperationException("Dynamic not allowed in HtmlMfe. Should use mfe() to manage an mfe view.");
    }


    @Override
    public <M, E extends Element> void visitAwait(E element, AwaitConsumer<E, M> asyncAction) {
        throw new UnsupportedOperationException("Await not allowed in HtmlView. Should use viewAsync() or viewSuspend() to manage an asynchronous view.");
    }

    @Override
    public <M, E extends Element> void visitSuspending(E element, SuspendConsumer<E, M> suspendAction) {
        throw new UnsupportedOperationException("Suspend not allowed in HtmlView. Should use viewAsync() or viewSuspend() to manage an asynchronous view.");
    }

    /**
     * Creates the last static HTML block.
     */
    @Override
    public void resolve(Object model) {
        String staticHtml = sb().substring(staticBlockIndex);
        HtmlContinuation staticCont = new HtmlContinuationSyncStatic(staticHtml.trim(), this, null);
        last = first == null
                ? first = staticCont         // assign both first and last
                : setNext(last, staticCont); // append new staticCont and return it to be the new last continuation.
    }
}
