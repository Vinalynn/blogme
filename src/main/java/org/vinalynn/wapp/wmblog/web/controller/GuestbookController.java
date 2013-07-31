package org.vinalynn.wapp.wmblog.web.controller;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vinalynn.wapp.wmblog.GlobalConst;

import javax.servlet.http.HttpServletRequest;

/**
 * User: caiwm
 * Date: 13-7-26
 * Time: AM 1:45
 */
@Controller
public class GuestbookController {

    /**
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/u-info.request")
    public String t_ServiceUser(HttpServletRequest request, Model model) throws Exception {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        String loginUrl = userService.createLoginURL(request.getRequestURI());
        if (null == user) {
            model.addAttribute(GlobalConst.FTL_COMMON_MSG_KEY,
                    "<a href='" + loginUrl + "'> Sign In </a>"
            );
        } else {
            model.addAttribute(GlobalConst.FTL_COMMON_MSG_KEY,
                    user.getNickname()
            );
        }

        return GlobalConst.FTL_COMMON_MSG_URL;
    }


}
