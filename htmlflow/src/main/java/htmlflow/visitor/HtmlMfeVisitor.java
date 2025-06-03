package htmlflow.visitor;

import htmlflow.HtmlMfeConfig;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.MfeConfiguration;
import org.xmlet.htmlapifaster.SuspendConsumer;
import org.xmlet.htmlapifaster.async.AwaitConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class HtmlMfeVisitor extends HtmlVisitor {
    private final List<HtmlMfeConfig> mfePage = new ArrayList<>();

    public HtmlMfeVisitor(Appendable out, boolean isIndented) {
        super(out, isIndented);
    }

    @Override
    public <E extends Element, U> void visitDynamic(E e, BiConsumer<E, U> biConsumer) {
        throw new IllegalStateException("Wrong use of dynamic() in a HtmlMfe! Use HtmlView to produce a dynamic view.");
    }

    @Override
    public <E extends Element> void visitMfe(E e, MfeConfiguration mfeConfiguration) {
        // collect the mfe configuration
        addMfePage((HtmlMfeConfig) mfeConfiguration);

        e.custom(mfeConfiguration.getMfeElementName()).addAttr("mfe-url", mfeConfiguration.getMfeUrlResource());
        e.getVisitor().visitAttribute("mfe-name", mfeConfiguration.getMfeName());
        e.getVisitor().visitAttribute("mfe-styling-url", mfeConfiguration.getMfeStylingUrl());
        e.getVisitor().visitAttribute("mfe-listen-event", mfeConfiguration.getMfeListeningEventName());
        e.getVisitor().visitAttribute("mfe-trigger-event", mfeConfiguration.getMfeTriggerEventName());
        if(mfeConfiguration.isMfeStreamingData()){
            e.getVisitor().visitAttribute("mfe-stream-data", String.valueOf(mfeConfiguration.isMfeStreamingData()));
        }

    }

    @Override
    public <M, E extends Element> void visitAwait(E e, AwaitConsumer<E, M> awaitConsumer) {
        throw new IllegalStateException("Wrong use of await() in HtmlMfe! Use HtmlFlow.viewAsync() to produce an async view.");
    }

    @Override
    public <M, E extends Element> void visitSuspending(E e, SuspendConsumer<E, M> suspendConsumer) {
        throw new IllegalStateException("Wrong use of suspending() in HtmlMfe! Use HtmlFlow.viewSuspend() to produce a suspendable view.");
    }

    @Override
    public void resolve(Object model) {
    }

    @Override
    public HtmlMfeVisitor clone(boolean isIndented) {
        return new HtmlMfeVisitor(out, isIndented);
    }

    public void addMfePage(HtmlMfeConfig mfePage) {
        this.mfePage.add(mfePage);
    }

    public List<HtmlMfeConfig> getMfePage() {
        return mfePage;
    }
}

