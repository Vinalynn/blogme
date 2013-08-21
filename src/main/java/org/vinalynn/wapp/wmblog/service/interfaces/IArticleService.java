package org.vinalynn.wapp.wmblog.service.interfaces;

import com.google.appengine.api.datastore.Query;
import org.vinalynn.wapp.wmblog.data.ArticleBean;
import org.vinalynn.wapp.wmblog.data.TagBean;

import java.util.List;

/**
 * User: caiwm
 * Date: 13-7-29
 * Time: AM 5:21
 */
public interface IArticleService {

    /**
     *
     * @param article
     * @param tags
     * @throws Exception
     */
    public void saveArticle(ArticleBean article, String tags) throws Exception;


    /**
     * save article interface
     *
     * @param article
     * @throws Exception
     */
    public void saveArticle(ArticleBean article) throws Exception;

    /**
     * @param articleBean
     * @return
     * @throws Exception
     */
    public String saveArticleWithUUIDRtn(ArticleBean articleBean) throws Exception;


    /**
     * @throws Exception
     */
    public List<ArticleBean> getArticle() throws Exception;

    /**
     * @param uuid
     * @throws Exception
     */
    public ArticleBean getArticleByUuid(String uuid) throws Exception;

    /**
     *
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    public List<ArticleBean> getArticles(
            int page,
            int pageSize,
            String sortPName,
            Query.SortDirection sortDirection
    ) throws Exception;

}
