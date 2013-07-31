package org.vinalynn.wapp.wmblog.data;

import com.google.appengine.api.datastore.Text;
import org.vinalynn.wapp.wmblog.annotations.GoogleStoreType;

/**
 * User: caiwm
 * Date: 13-7-29
 * Time: AM 4:56
 */
public class ArticleBean extends DataBean {
    public String title;
    public String owner;
    @GoogleStoreType(clazz = Text.class)
    public String content;

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

}
