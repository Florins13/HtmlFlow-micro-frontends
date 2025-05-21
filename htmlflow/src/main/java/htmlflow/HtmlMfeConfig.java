package htmlflow;


import org.xmlet.htmlapifaster.MfeConfiguration;

public class HtmlMfeConfig implements MfeConfiguration {
    private final String mfeUrlResource;
    private final String mfeElementName;
    private final String mfeListeningEventName;
    private final String mfeTriggersEventName;
    private final String mfeScriptUrl;
    private final String mfeCssName;

    public HtmlMfeConfig(String mfeUrlResource, String mfeElementName, String mfeListeningEventName, String mfeTriggersEventName, String mfeScriptUrl, String mfeCssName) {
        this.mfeCssName = mfeCssName;
        this.mfeUrlResource = mfeUrlResource;
        this.mfeElementName = mfeElementName;
        this.mfeListeningEventName = mfeListeningEventName;
        this.mfeTriggersEventName = mfeTriggersEventName;
        this.mfeScriptUrl = mfeScriptUrl;
    }

    @Override
    public String getMfeUrlResource() {
        return mfeUrlResource;
    }

    @Override
    public String getMfeElementName() {
        return mfeElementName;
    }

    @Override
    public String getMfeListeningEventName() {
        return mfeListeningEventName;
    }

    @Override
    public String getMfeTriggerEventName() {
        return mfeTriggersEventName;
    }

    @Override
    public String getMfeScriptUrl() {
        return mfeScriptUrl;
    }

    @Override
    public String getMfeCssName() {
        return mfeCssName;
    }
}
