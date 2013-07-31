package org.vinalynn.wapp.wmblog.service;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.vinalynn.wapp.wmblog.data.ArticleBean;
import org.vinalynn.wapp.wmblog.service.interfaces.IArticleService;

import java.util.List;


/**
 * User: caiwm
 * Date: 13-7-31
 * Time: 下午8:47
 */
public class TestArticleService {

    private transient static Logger log = Logger.getLogger(TestArticleService.class);


    private ApplicationContext ac;

    @Before
    public void setUp() throws Exception {
        ac = new ClassPathXmlApplicationContext("application.xml");

    }

    @Test
    public void testGetArticleByUUID() throws Exception{
        IArticleService iArticleService = ac.getBean(IArticleService.class);
        List<ArticleBean> beans = iArticleService.getArticle();
        log.error(beans);
    }

}
