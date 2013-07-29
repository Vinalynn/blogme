package org.vinalynn.wapp.wmblog.service.impl;

import org.apache.log4j.Logger;
import org.vinalynn.wapp.wmblog.GlobalConst;
import org.vinalynn.wapp.wmblog.data.ArticleBean;
import org.vinalynn.wapp.wmblog.data.DataBean;
import org.vinalynn.wapp.wmblog.service.interfaces.IArticleService;
import org.vinalynn.wapp.wmblog.util.GoogleDataStoreUtil;

import java.util.List;

/**
 * use google-datastore to Persistence data
 * <p/>
 * User: caiwm
 * Date: 13-7-29
 * Time: 下午5:23
 */
public class ArticleServiceImpl implements IArticleService {

    private transient static Logger log = Logger.getLogger(ArticleServiceImpl.class);

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
    public void getArticle() throws Exception {
        List<DataBean> list = GoogleDataStoreUtil.getDatas(GlobalConst.KIND_ARTICLE, ArticleBean.class, null);
        if (log.isInfoEnabled()) {
            log.info(list);
        }
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
}
