package org.vinalynn.wapp.wmblog.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vinalynn.wapp.wmblog.GlobalConst;
import org.vinalynn.wapp.wmblog.data.ArticleBean;
import org.vinalynn.wapp.wmblog.service.interfaces.IArticleService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * User: caiwm
 * Date: 13-7-29
 * Time: AM 9:00
 */
@Controller
public class TestController {

    IArticleService iArticleService;

    public void setiArticleService(IArticleService iArticleService) {
        this.iArticleService = iArticleService;
    }


    @RequestMapping(value = "/test-main.request")
    public String testMain(HttpServletRequest request, Model model) throws Exception {

        //testSaveSingleObject();
        getArticle();

        //model.addAttribute(GlobalConst.FTL_COMMON_MSG_KEY, getArticle());
        return GlobalConst.FTL_COMMON_INDEX;
    }

    private void testSaveSingleObject() throws Exception {
        ArticleBean articleBean = new ArticleBean();
        articleBean.setKind(GlobalConst.KIND_ARTICLE);
        articleBean.setContent("this is the test");
        articleBean.setOwner("caiwm");
        articleBean.setTitle("Test Article");
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

}
