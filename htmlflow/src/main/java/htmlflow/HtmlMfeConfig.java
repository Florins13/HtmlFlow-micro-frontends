package htmlflow;


import org.xmlet.htmlapifaster.MfeConfiguration;

public class HtmlMfeConfig implements MfeConfiguration {
    private String MFE_URL_RESOURCE = "";
    private String MFE_ELEMENT_NAME = "";
    private String MFE_LISTENING_EVENT_NAME = "";
    private String MFE_TRIGGERS_EVENT_NAME = "";
    private String MFE_SCRIPT_NAME = "";
    private String MFE_CSS_NAME = "";
    private boolean MFE_MULTIPLE_BUTTONS = false;

    public HtmlMfeConfig(String MFE_URL_RESOURCE, String MFE_ELEMENT_NAME, String MFE_LISTENING_EVENT_NAME, String MFE_TRIGGERS_EVENT_NAME, String MFE_SCRIPT_NAME, String MFE_CSS_NAME, boolean MFE_MULTIPLE_BUTTONS) {
        this.MFE_CSS_NAME = MFE_CSS_NAME;
        this.MFE_URL_RESOURCE = MFE_URL_RESOURCE;
        this.MFE_ELEMENT_NAME = MFE_ELEMENT_NAME;
        this.MFE_LISTENING_EVENT_NAME = MFE_LISTENING_EVENT_NAME;
        this.MFE_TRIGGERS_EVENT_NAME = MFE_TRIGGERS_EVENT_NAME;
        this.MFE_SCRIPT_NAME = MFE_SCRIPT_NAME;
        this.MFE_MULTIPLE_BUTTONS = MFE_MULTIPLE_BUTTONS;
    }

    public String getMFE_URL_RESOURCE() {
        return MFE_URL_RESOURCE;
    }

    public String getMFE_ELEMENT_NAME() {
        return MFE_ELEMENT_NAME;
    }

    public String getMFE_LISTENING_EVENT_NAME() {
        return MFE_LISTENING_EVENT_NAME;
    }

    public String getMFE_TRIGGERS_EVENT_NAME() {
        return MFE_TRIGGERS_EVENT_NAME;
    }

    public String getMFE_SCRIPT_NAME() {
        return MFE_SCRIPT_NAME;
    }

    public String getMFE_CSS_NAME() {
        return MFE_CSS_NAME;
    }

    public boolean isMFE_MULTIPLE_BUTTONS() {
        return MFE_MULTIPLE_BUTTONS;
    }
}
