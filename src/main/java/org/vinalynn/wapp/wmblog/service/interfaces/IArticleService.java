package org.vinalynn.wapp.wmblog.service.interfaces;

import org.vinalynn.wapp.wmblog.data.ArticleBean;

/**
 * User: caiwm
 * Date: 13-7-29
 * Time: обнГ5:21
 */
public interface IArticleService {

    /**
     * save article interface
     *
     * @param article
     * @throws Exception
     */
    public void saveArticle(ArticleBean article) throws Exception;

    /**
     *
     * @param articleBean
     * @return
     * @throws Exception
     */
    public String saveArticleWithUUIDRtn(ArticleBean articleBean) throws Exception;


    /**
     *
     * @throws Exception
     */
    public void getArticle() throws Exception;

}
