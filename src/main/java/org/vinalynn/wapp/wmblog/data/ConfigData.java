package org.vinalynn.wapp.wmblog.data;

/**
 * User: caiwm
 * Date: 13-8-2
 * Time: 下午9:30
 */
public class ConfigData extends DataBean {
    private String configKey;
    private String configValue;
    private String state;
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
