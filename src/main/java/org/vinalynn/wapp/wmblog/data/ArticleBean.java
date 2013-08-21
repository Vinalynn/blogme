package org.vinalynn.wapp.wmblog.data;

import com.google.appengine.api.datastore.Text;
import org.vinalynn.wapp.wmblog.annotations.GoogleStoreAction;

import java.util.Date;

/**
 * User: caiwm
 * Date: 13-7-29
 * Time: AM 4:56
 */
public class ArticleBean extends DataBean {
    public String title;
    public String owner;
    @GoogleStoreAction(storeType = Text.class)
    public String content;
    @GoogleStoreAction(storeType = Text.class)
    public String text;
    public String url;
    public java.util.Date createDate;
    public String state;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
