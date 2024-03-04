package personal.css.UniversalSpringbootProject.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static personal.css.UniversalSpringbootProject.common.consts.MyConst.EXPIRATION_TIME;

/**
 * @Description: Cookie相关工具
 * @Author: CSS
 * @Date: 2024/3/5 0:30
 */
public class CookieUtil {

    /**
     * 将数据存储到客户端Cookie，保存时间为过期时间的两倍
     *
     * @param response
     * @param k        键名
     * @param v        值
     */
    public static void setCookie(HttpServletResponse response, String k, String v) {
        Cookie cookie = new Cookie(k, v);
        cookie.setPath("/");
        cookie.setMaxAge((int) (EXPIRATION_TIME * 2 / 1000));
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }


    /**
     * 从客户端获取Cookie的值
     *
     * @param httpServletRequest
     * @param cookieName
     * @return
     */
    public static String getValueFromCookies(HttpServletRequest httpServletRequest, String cookieName) throws Exception {
        try {
            Cookie[] cookies = httpServletRequest.getCookies();
            if(null == cookies)
                return null;
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                String name = cookie.getName();
                if (cookieName.equals(name)) {
                    return cookie.getValue();
                }
            }
            return null;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    /**
     * 将cookie从客户端删除
     *
     * @param cookieName
     * @param response
     */
    public static void removeCookie(String cookieName, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");//一定要和之前设置的一样
        response.addCookie(cookie);
    }
}
