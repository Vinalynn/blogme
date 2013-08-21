package org.vinalynn.wapp.wmblog.web.controller;

import com.google.appengine.api.datastore.Query;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vinalynn.wapp.wmblog.GlobalConst;
import org.vinalynn.wapp.wmblog.data.ArticleBean;
import org.vinalynn.wapp.wmblog.service.interfaces.IArticleService;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * User: caiwm
 * Date: 13-7-29
 * Time: AM 9:00
 */
@Controller
public class TestController {

    private transient static Logger log = Logger.getLogger(TestController.class);

    IArticleService iArticleService;

    public void setiArticleService(IArticleService iArticleService) {
        this.iArticleService = iArticleService;
    }


    @RequestMapping(value = "/test-main.request")
    public String testMain(HttpServletRequest request, Model model) throws Exception {

        testSaveSingleObject();
        //getArticle();
        //getArticleByUUID();
        //model.addAttribute(GlobalConst.FTL_COMMON_MSG_KEY, getArticle());
        //getArticleByCondition();
        return GlobalConst.FTL_COMMON_INDEX;
    }

    private void testSaveSingleObject() throws Exception {
        ArticleBean articleBean = new ArticleBean();
        articleBean.setKind(GlobalConst.KIND_ARTICLE);
        articleBean.setContent("this is the test");
        articleBean.setOwner("caiwm");
        articleBean.setTitle("Test Article");
        articleBean.setCreateDate(new Date(System.currentTimeMillis()));
        articleBean.setState("U");
        articleBean.setUrl("/this-is-test.html");
        this.iArticleService.saveArticleWithUUIDRtn(articleBean);
        this.iArticleService.saveArticleWithUUIDRtn(articleBean);
    }

    private String getArticle() throws Exception {
        List<ArticleBean> list = this.iArticleService.getArticle();
        if (null == list) return StringUtils.EMPTY;
        StringBuilder sb = new StringBuilder();
        for (ArticleBean bean : list) {
            sb.append(bean.toString() + "<br>");
        }
        return sb.toString();
    }

    private void getArticleByUUID() throws Exception {
        ArticleBean bean = this.iArticleService.getArticleByUuid("020ee6ae-9033-4ae5-b3e1-394648e5ffef");
        log.error(bean);
    }

    private void getArticleByCondition() throws Exception{
        List<ArticleBean> lists =
                this.iArticleService.getArticles(0, 10, "createDate", Query.SortDirection.DESCENDING);
        List<ArticleBean> lists2 =
                this.iArticleService.getArticles(0, 10, "createDate", Query.SortDirection.DESCENDING);
        List<ArticleBean> lists3 =
                this.iArticleService.getArticles(0, 10, "createDate", Query.SortDirection.DESCENDING);
        List<ArticleBean> lists4 =
                this.iArticleService.getArticles(0, 10, "createDate", Query.SortDirection.DESCENDING);
        log.error(lists);
    }

}
