package org.vinalynn.wapp.wmblog.data;

/**
 * User: caiwm
 * Date: 13-7-29
 * Time: aM 5:09
 */
public abstract class DataBean {
    private String kind;
    private String uuid;

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getKind() {
        return kind;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
