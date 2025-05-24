package htmlflow;


import org.xmlet.htmlapifaster.MfeConfiguration;

public class HtmlMfeConfig implements MfeConfiguration {
    private final String mfeUrlResource;
    private final String mfeName;
    private final String mfeListeningEventName;
    private final String mfeTriggersEventName;
    private String mfeElementName = "micro-frontend";
    private String mfeScriptUrl = null;
    private String mfeStylingUrl = null;

    public HtmlMfeConfig(String mfeUrlResource, String mfeName, String mfeListeningEventName, String mfeTriggersEventName) {
        this.mfeUrlResource = mfeUrlResource;
        this.mfeName = mfeName;
        this.mfeListeningEventName = mfeListeningEventName;
        this.mfeTriggersEventName = mfeTriggersEventName;
    }

    public HtmlMfeConfig(String mfeUrlResource, String mfeName, String mfeListeningEventName, String mfeTriggersEventName, String mfeScriptUrl, String mfeStylingUrl) {
        this(mfeUrlResource, mfeName, mfeListeningEventName, mfeTriggersEventName);
        this.mfeScriptUrl = mfeScriptUrl;
        this.mfeStylingUrl = mfeStylingUrl;
    }

    public HtmlMfeConfig(String mfeUrlResource, String mfeName, String mfeElementName, String mfeListeningEventName, String mfeTriggersEventName, String mfeScriptUrl, String mfeStylingUrl) {
        this(mfeUrlResource, mfeName, mfeListeningEventName, mfeTriggersEventName, mfeScriptUrl, mfeStylingUrl);
        this.mfeElementName = mfeElementName;
    }

    @Override
    public String getMfeUrlResource() {
        return mfeUrlResource;
    }

    @Override
    public String getMfeName() { return mfeName; }
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
    public String getMfeStylingUrl() {
        return mfeStylingUrl;
    }
}
