package com.mayj.demo.context;

import lombok.Data;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.Supplier;

/**
 * @Author mayunjie
 * @Date 2022/7/22 11:21
 **/
@Data
public class WxAppletRequestContext {

    private static final ThreadLocal<WxAppletRequestContext> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 获得当前请求上下文
     *
     * @return 上下文对象
     */
    public static WxAppletRequestContext getContext() {
        WxAppletRequestContext requestContext = THREAD_LOCAL.get();
        if (requestContext == null) {
            requestContext = new WxAppletRequestContext();
            THREAD_LOCAL.set(requestContext);
        }
        return requestContext;
    }

    /**
     * 获取 HttpServletRequest
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    public static HttpServletResponse getResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : ((ServletRequestAttributes) requestAttributes).getResponse();
    }

    public static <T> T inContext(WxAppletRequestContext context, Supplier<T> fn) {
        WxAppletRequestContext oldContext = getContext();
        setContext(context);
        T t = fn.get();
        setContext(oldContext);
        return t;
    }

    /**
     * 设置当前请求上下文
     */
    private static void setContext(WxAppletRequestContext requestContext) {
        THREAD_LOCAL.set(requestContext);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
