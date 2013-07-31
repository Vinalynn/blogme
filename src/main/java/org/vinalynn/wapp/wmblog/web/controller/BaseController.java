package org.vinalynn.wapp.wmblog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vinalynn.wapp.wmblog.GlobalConst;

import javax.servlet.http.HttpServletRequest;

/**
 * User: caiwm
 * Date: 13-7-29
 * Time: AM 9:00
 */
@Controller
public class BaseController {

    @RequestMapping(value = "/default.html")
    public String get_DefaultPage(HttpServletRequest request, Model model) throws Exception {
        model.addAttribute(GlobalConst.FTL_COMMON_MSG_KEY, "This application is coming soon...");
        return GlobalConst.FTL_COMMON_INDEX;
    }

}
