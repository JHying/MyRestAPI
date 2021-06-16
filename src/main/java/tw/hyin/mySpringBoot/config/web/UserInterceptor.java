package tw.hyin.mySpringBoot.config.web;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import tw.hyin.mySpringBoot.pojo.LoginInfo;
import tw.hyin.mySpringBoot.utils.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author rita6 on 2021.
 */
@Component
public class UserInterceptor implements HandlerInterceptor {

    public static LoginInfo USER;
    public static final String userTag = "user";

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
            throws Exception {
        //確認登入狀態
        HttpSession session = req.getSession();
        USER = (LoginInfo) session.getAttribute(userTag);
        if (USER == null) {
            Log.error("request ERROR, URL: " + req.getRequestURL());
            throw new Exception("User not found or unavailable.");
        }
        return true;
    }

}
