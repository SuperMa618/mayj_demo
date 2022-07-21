package com.mayj.demo.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author mayunjie
 * @Date 2022/7/21 16:09
 **/
public class RequestHelper {

    /**
     * 获取请求所有入参（过滤不需要的）
     * @param request request
     * @return map
     */
    public static Map<String, String> getRequestParameter(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, String> result = new HashMap<>(16);
        if (null != requestMap && requestMap.size() > 0) {
            for (String key : requestMap.keySet()) {
                String[] values = requestMap.get(key);
                //日常环境先过滤掉这里的isg参数 去除日常环境淘宝自带参数
                if (!key.startsWith("isg") && !"_tbScancodeApproach_".equals(key) && !"ttid".equals(key)) {
                    result.put(Helper.underline(key), null != values && values.length > 0 ? values[0] : "");
                }
            }
        }
        return result;
    }

    /**
     * getRequest
     * @return request
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest();
    }

    /**
     * getResponse
     * @return response
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        return attributes.getResponse();
    }

    /**
     * getCookie
     * @param cookieName string
     * @return string
     */
    public static String getCookie(String cookieName) {
        HttpServletRequest request = getRequest();
        Cookie[] cookies = request.getCookies();
        if (null != cookies && !Helper.empty(cookieName)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * setCookie
     * @param cookieName string
     * @param cookieValue string
     * @param expiry integer
     * @param path string
     */
    public static void setCookie(String cookieName, String cookieValue, Integer expiry, String path) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath(path);
        cookie.setMaxAge(expiry);
        HttpServletResponse response = getResponse();
        response.addCookie(cookie);
    }
}
