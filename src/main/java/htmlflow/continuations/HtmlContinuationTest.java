package htmlflow.continuations;

import htmlflow.visitor.HtmlVisitor;
import org.xmlet.htmlapifaster.Element;

public class HtmlContinuationTest<E extends Element>  extends HtmlContinuationSync{
    /**
     * The continuation that consumes the element and a model.
     */
    /**
     * The element passed to the continuation consumer.
     */
    final E element;


    /**
     * @param currentDepth Indentation depth associated to this block.
     * @param isClosed
     * @param visitor
     * @param next
     */
    public HtmlContinuationTest(int currentDepth, boolean isClosed, E element ,HtmlVisitor visitor, HtmlContinuation next) {
        super(currentDepth, isClosed, visitor, next);
        this.element = element;
    }

    @Override
    public HtmlContinuation copy(HtmlVisitor v) {
        return new HtmlContinuationTest<>(
                currentDepth,
                isClosed,
                copyElement(element, v),
                v,
                next != null ? next.copy(v) : null); // call copy recursively
    }

    @Override
    protected void emitHtml(Object model) {
        System.out.println(this.element);
        visitor.write("EMIT HTML");
    }
}
