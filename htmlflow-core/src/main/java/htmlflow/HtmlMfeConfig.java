package htmlflow;

import org.xmlet.htmlapifaster.MfeConfiguration;
import org.xmlet.htmlapifaster.MfeConfigurationBuilder;

//public class HtmlMfeConfig implements MfeConfiguration {
//
//    private String mfeUrlResource;
//    private String mfeName;
//    private String mfeListeningEventName;
//    private String mfeTriggersEventName;
//    private String mfeElementName = "micro-frontend";
//    private String mfeScriptUrl = null;
//    private String mfeStylingUrl = null;
//    private boolean isMfeStreamingData;
//
//    @Override
//    public String getMfeUrlResource() {
//        return mfeUrlResource;
//    }
//
//    @Override
//    public String getMfeName() {
//        return mfeName;
//    }
//
//    @Override
//    public String getMfeElementName() {
//        return mfeElementName;
//    }
//
//    @Override
//    public String getMfeListeningEventName() {
//        return mfeListeningEventName;
//    }
//
//    @Override
//    public String getMfeTriggerEventName() {
//        return mfeTriggersEventName;
//    }
//
//    @Override
//    public String getMfeScriptUrl() {
//        return mfeScriptUrl;
//    }
//
//    @Override
//    public String getMfeStylingUrl() {
//        return mfeStylingUrl;
//    }
//
//    @Override
//    public boolean isMfeStreamingData() {
//        return this.isMfeStreamingData;
//    }
//
//    @Override
//    public MfeConfiguration setMfeStreamingData(boolean mfeStreamingData) {
//        this.isMfeStreamingData = mfeStreamingData;
//        return this;
//    }
//
//    @Override
//    public MfeConfiguration setMfeUrlResource(String mfeUrlResource) {
//        this.mfeUrlResource = mfeUrlResource;
//        return this;
//    }
//
//    @Override
//    public MfeConfiguration setMfeName(String mfeName) {
//        this.mfeName = mfeName;
//        return this;
//    }
//
//    @Override
//    public MfeConfiguration setMfeListeningEventName(
//        String mfeListeningEventName
//    ) {
//        this.mfeListeningEventName = mfeListeningEventName;
//        return this;
//    }
//
//    @Override
//    public MfeConfiguration setMfeTriggersEventName(
//        String mfeTriggersEventName
//    ) {
//        this.mfeTriggersEventName = mfeTriggersEventName;
//        return this;
//    }
//
//    @Override
//    public MfeConfiguration setMfeElementName(String mfeElementName) {
//        this.mfeElementName = mfeElementName;
//        return this;
//    }
//
//    @Override
//    public MfeConfiguration setMfeScriptUrl(String mfeScriptUrl) {
//        this.mfeScriptUrl = mfeScriptUrl;
//        return this;
//    }
//
//    @Override
//    public MfeConfiguration setMfeStylingUrl(String mfeStylingUrl) {
//        this.mfeStylingUrl = mfeStylingUrl;
//        return this;
//    }
//}

public final class HtmlMfeConfig implements MfeConfiguration {

    private final String mfeUrlResource;
    private final String mfeName;
    private final String mfeListeningEventName;
    private final String mfeTriggersEventName;
    private final String mfeElementName;
    private final String mfeScriptUrl;
    private final String mfeScriptIntegrity;
    private final String mfeStylingUrl;
    private final boolean isMfeStreamingData;

    private HtmlMfeConfig(Builder builder) {
        this.mfeUrlResource = builder.mfeUrlResource;
        this.mfeName = builder.mfeName;
        this.mfeListeningEventName = builder.mfeListeningEventName;
        this.mfeTriggersEventName = builder.mfeTriggersEventName;
        this.mfeElementName = builder.mfeElementName;
        this.mfeScriptUrl = builder.mfeScriptUrl;
        this.mfeStylingUrl = builder.mfeStylingUrl;
        this.mfeScriptIntegrity = builder.mfeScriptIntegrity;
        this.isMfeStreamingData = builder.isMfeStreamingData;
    }

    @Override public String getMfeUrlResource()        { return mfeUrlResource; }
    @Override public String getMfeName()               { return mfeName; }
    @Override public String getMfeElementName()         { return mfeElementName; }
    @Override public String getMfeListeningEventName()  { return mfeListeningEventName; }
    @Override public String getMfeTriggerEventName()    { return mfeTriggersEventName; }
    @Override public String getMfeScriptUrl()           { return mfeScriptUrl; }
    @Override public String getMfeStylingUrl()          { return mfeStylingUrl; }
    @Override public String getMfeScriptIntegrity()     { return mfeScriptIntegrity;}
    @Override public boolean isMfeStreamingData()       {return isMfeStreamingData;}


    public static class Builder implements MfeConfigurationBuilder {
        private String mfeUrlResource;
        private String mfeName;
        private String mfeListeningEventName;
        private String mfeTriggersEventName;
        private String mfeElementName = "micro-frontend";
        private String mfeScriptUrl;
        private String mfeScriptIntegrity;
        private String mfeStylingUrl;
        private boolean isMfeStreamingData;

        @Override public Builder setMfeUrlResource(String v)        { this.mfeUrlResource = v; return this; }
        @Override public Builder setMfeName(String v)               { this.mfeName = v; return this; }
        @Override public Builder setMfeListeningEventName(String v) { this.mfeListeningEventName = v; return this; }
        @Override public Builder setMfeTriggersEventName(String v)  { this.mfeTriggersEventName = v; return this; }
        @Override public Builder setMfeElementName(String v)        { this.mfeElementName = v; return this; }
        @Override public Builder setMfeScriptUrl(String v)          { this.mfeScriptUrl = v; return this; }
        @Override public Builder setMfeStylingUrl(String v)         { this.mfeStylingUrl = v; return this; }
        @Override public Builder setMfeScriptIntegrity(String v)    { this.mfeScriptIntegrity = v; return this;}
        @Override public Builder setMfeStreamingData(boolean v)     { this.isMfeStreamingData = v; return this; }

        // Read-only getters (from MfeConfiguration via MfeConfigurationBuilder)
        @Override public String getMfeUrlResource()        { return mfeUrlResource; }
        @Override public String getMfeName()               { return mfeName; }
        @Override public String getMfeElementName()         { return mfeElementName; }
        @Override public String getMfeListeningEventName()  { return mfeListeningEventName; }
        @Override public String getMfeTriggerEventName()    { return mfeTriggersEventName; }
        @Override public String getMfeScriptUrl()           { return mfeScriptUrl; }
        @Override public String getMfeStylingUrl()          { return mfeStylingUrl; }
        @Override public String getMfeScriptIntegrity()     { return mfeScriptIntegrity;}
        @Override public boolean isMfeStreamingData()       { return isMfeStreamingData; }

        public HtmlMfeConfig build() {
            if (mfeUrlResource == null || mfeUrlResource.isBlank()) {
                throw new IllegalStateException("mfeUrlResource is required");
            }
            if (mfeName == null || mfeName.isBlank()) {
                throw new IllegalStateException("mfeName is required");
            }
            return new HtmlMfeConfig(this);
        }
    }
}
