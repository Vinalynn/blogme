package org.vinalynn.wapp.wmblog.web.interceptor;

import com.google.appengine.api.datastore.Transaction;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.vinalynn.wapp.wmblog.GlobalConst;
import org.vinalynn.wapp.wmblog.nbeans.BlogInfo;
import org.vinalynn.wapp.wmblog.util.GoogleDataStoreUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * User: caiwm
 * Date: 13-7-30
 */
public class BaseRequestHandlerInterceptor implements HandlerInterceptor {
    private transient static Logger log =
            Logger.getLogger(BaseRequestHandlerInterceptor.class);

    private static ThreadLocal<Object> tl = new ThreadLocal<Object>();

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
//        String reqUri = request.getRequestURI();
//        if(StringUtils.contains(reqUri, ".wr")){
//            HashMap<String, Object> tlMap = new HashMap<String, Object>();
//            Transaction tx =  GoogleDataStoreUtil.beginTransaction();
//
//            tlMap.put("wr-tx", tx);
//            tl.set(tlMap);
//            log.info("");
//        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //log.error("postHandle");
        BlogInfo bi = new BlogInfo();
        bi.setVersion(RandomStringUtils.random(5, false, true));
        modelAndView.addObject(GlobalConst.KEY_BLOG_INFO, bi);
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //log.error("afterCompletion");
        //currently do nothing.
    }
}
