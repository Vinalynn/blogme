package org.vinalynn.wapp.wmblog.service.impl;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import org.apache.log4j.Logger;
import org.vinalynn.wapp.wmblog.GlobalConst;
import org.vinalynn.wapp.wmblog.data.ArticleBean;
import org.vinalynn.wapp.wmblog.data.DataBean;
import org.vinalynn.wapp.wmblog.data.TagBean;
import org.vinalynn.wapp.wmblog.service.interfaces.IArticleService;
import org.vinalynn.wapp.wmblog.service.interfaces.IEntityRelationService;
import org.vinalynn.wapp.wmblog.service.interfaces.ITagService;
import org.vinalynn.wapp.wmblog.util.GoogleDataStoreUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * use google-datastore to Persistence data
 * <p/>
 * User: caiwm
 * Date: 13-7-29
 * Time:  PM 5:23
 */
public class ArticleServiceImpl implements IArticleService {

    private transient static Logger log = Logger.getLogger(ArticleServiceImpl.class);

    ITagService iTagService;
    IEntityRelationService iEntityRelationService;

    public void setiEntityRelationService(IEntityRelationService iEntityRelationService) {
        this.iEntityRelationService = iEntityRelationService;
    }

    public void setiTagService(ITagService iTagService) {
        this.iTagService = iTagService;
    }

    /**
     * 调用谷歌的持久化接口DatastoreService，对数据进行持久化
     *
     * @param article
     * @throws Exception
     */
    @Override
    public void saveArticle(ArticleBean article) throws Exception {
        GoogleDataStoreUtil.storeSingleBean(article);
    }

    @Override
    public List<ArticleBean> getArticle() throws Exception {
        List<ArticleBean> list = GoogleDataStoreUtil.getDatas(GlobalConst.KIND_ARTICLE, ArticleBean.class, null);
        return (null != list && list.size() > 0) ? list : null;
    }

    /**
     * @param articleBean
     * @return
     * @throws Exception
     */
    @Override
    public String saveArticleWithUUIDRtn(ArticleBean articleBean) throws Exception {
        String uuid = GoogleDataStoreUtil.storeSingleDataWithUUIDRtn(articleBean);
        if (log.isInfoEnabled()) {
            log.info("Data saved. uuid=[" + uuid + "]");
        }
        return uuid;
    }

    @Override
    public ArticleBean getArticleByUuid(String uuid) throws Exception {
//        Query.Filter uuidFilter = new Query.FilterPredicate(
//                GlobalConst.FILTER_UUID,
//                Query.FilterOperator.EQUAL,
//                uuid
//        );
//        List<ArticleBean> l = GoogleDataStoreUtil.getDatas(
//                GlobalConst.KIND_ARTICLE,
//                ArticleBean.class,
//                new Query.Filter[]{uuidFilter}
//        );
//        return (null != l && l.size() > 0) ? l.get(0) : null;
        return GoogleDataStoreUtil.getDataByKey(
                KeyFactory.createKey(GlobalConst.KIND_ARTICLE, uuid),
                ArticleBean.class
        );
    }

    @Override
    public List<ArticleBean> getArticles(int page, int pageSize,
                                         String sortPName,
                                         Query.SortDirection sortDirection) throws Exception {

        Query query = new Query(GlobalConst.KIND_ARTICLE);
        return GoogleDataStoreUtil.executeQuery(query, page, pageSize, sortPName,
                sortDirection, null, ArticleBean.class);
    }

    @Override
    public void saveArticle(ArticleBean article, String tags) throws Exception {
        //先查询该文章是否存在
        if (isArticleExists(article.getUrl(), GlobalConst.STATE_VALID)) {
            throw new Exception("article<" + article.getUrl() + "> exists.");
        }
        HashMap<String, String> tgs = this.iTagService.parseTags(tags);

        //保存文章
        String aUUID = saveArticleWithUUIDRtn(article);
        //保存文章与Tag的关系
        Iterator<Map.Entry<String, String>> iterator = tgs.entrySet().iterator();
        HashMap<String, String> relations = new HashMap<String, String>();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            relations.put(entry.getValue(), aUUID);
        }
        this.iEntityRelationService.createEntityRelation(relations, GlobalConst.RELATION_ARTICLE_TAG);
    }

    /**
     * 根据文章的Url路径查询文章列表，一般情况下这个结果集只有一个
     *
     * @param url 文章的英文路径
     * @return ArrayList&lt;ArticleBean&gt;
     * @throws Exception
     */
    private List<ArticleBean> getArticleByUrl(String url, String state) throws Exception {
        Query.Filter urlFilter = new Query.FilterPredicate(
                "url",
                Query.FilterOperator.EQUAL,
                url
        );
        Query.Filter stateFilter = new Query.FilterPredicate(
                "state",
                Query.FilterOperator.EQUAL,
                state
        );
        List<ArticleBean> articles = GoogleDataStoreUtil.getDatas(GlobalConst.KIND_ARTICLE,
                ArticleBean.class, new Query.Filter[]{urlFilter, stateFilter});
        return articles;
    }

    /**
     * 判断英文路径为url的文章是否存在
     *
     * @param url 文章的Url路径
     * @return 如果存在返回true, 否则返回false
     * @throws Exception
     */
    private boolean isArticleExists(String url, String state) throws Exception {
        List<ArticleBean> articles = getArticleByUrl(url, state);
        return (null != articles && articles.size() > 0)
                ? Boolean.TRUE : Boolean.FALSE;
    }
}
