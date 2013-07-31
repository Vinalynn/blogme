package org.vinalynn.wapp.wmblog.web.interceptor;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.vinalynn.wapp.wmblog.GlobalConst;
import org.vinalynn.wapp.wmblog.nbeans.BlogInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: caiwm
 * Date: 13-7-30
 * Time: ÏÂÎç11:07
 */
public class BaseRequestHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        BlogInfo bi = new BlogInfo();
        bi.setVersion(RandomStringUtils.random(5, false, true));
        modelAndView.addObject(GlobalConst.KEY_BLOG_INFO, bi);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        //currently do nothing.
    }
}
