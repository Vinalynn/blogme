package org.vinalynn.wapp.wmblog.data;

/**
 * 标签
 * User: caiwm
 * Date: 13-8-21
 * Time: 下午2:58
 */
public class TagBean extends DataBean {
    public String tagName;
    public String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
