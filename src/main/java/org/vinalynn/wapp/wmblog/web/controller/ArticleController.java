package org.vinalynn.wapp.wmblog.web.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.vinalynn.wapp.wmblog.GlobalConst;
import org.vinalynn.wapp.wmblog.data.ArticleBean;
import org.vinalynn.wapp.wmblog.service.interfaces.IArticleService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * User: caiwm
 * Date: 13-8-21
 * Time: 上午10:46
 */
@Controller
public class ArticleController {
    private transient static Logger log = Logger.getLogger(ArticleController.class);
    IArticleService iArticleService;

    public void setiArticleService(IArticleService iArticleService) {
        this.iArticleService = iArticleService;
    }

    @RequestMapping(value = "/article-post.html")
    public String postArticle(HttpServletRequest request, Model model) throws Exception{
        return GlobalConst.FTL_ARTICLE_POSTER;
    }

    @RequestMapping(value = "/save-article.cgi")
    public String saveArticle(HttpServletRequest request, Model model,
                              @RequestParam(value = "enUrl", required = true) String enUrl,
                              @RequestParam(value="aTitle", required = true) String articleTitle,
                              @RequestParam(value="aTags", required = true) String aTags,
                              @RequestParam(value="aHtmlValue", required = true) String aHtmlValue,
                              @RequestParam(value="aTextValue", required = true) String aTextValue
    ) throws Exception{

        ArticleBean article = new ArticleBean();
        article.setState(GlobalConst.STATE_VALID);
        article.setUrl(enUrl);
        article.setCreateDate(new Date(System.currentTimeMillis()));
        article.setContent(aHtmlValue);
        article.setText(aTextValue);
        article.setTitle(articleTitle);
        article.setKind(GlobalConst.KIND_ARTICLE);

        String msg = "OK";
        try{
            //保存博文的接口
            this.iArticleService.saveArticle(article, aTags);
        } catch (Exception e){
            msg = e.getMessage();
            log.error(null, e);
        }
        model.addAttribute(GlobalConst.FTL_COMMON_MSG_KEY, msg);
        return GlobalConst.FTL_COMMON_MSG_URL;
    }

}
