package com.mayj.demo.config.aspect;

import com.mayj.demo.context.WxAppletRequestContext;
import com.mayj.demo.utils.JsonUtil;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author mayunjie
 * @Date 2022/7/22 11:22
 **/
@Slf4j
@Aspect
@Component
@Order(1)
public class WxAppletInterfaceAspect {
//    @Resource
//    private WxAppletUserService userSessionService;
//    @Resource
//    private RegisterUserService registerUserService;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)"
            + "||@annotation(org.springframework.web.bind.annotation.PostMapping)"
            + "||@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void controller() {
    }

    @Pointcut("execution(* com.alibaba.sports.ta.app.wechat..*Controller.*(..))")
    public void wxController() {
    }

    @Before("controller()")
    public void aroundController(JoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
//        EagleeyeLogConverter.autoStartTrace(joinPoint);
        String url = request.getRequestURL().toString();
        if (url.contains("/wechat/")) {
//            EagleeyeLogConverter.setSource("wechat", request.getRequestURI());
        } else {
//            EagleeyeLogConverter.setSource("HTTP", request.getRequestURI());
        }
    }


    @Around("wxController()")
    public Object aroundWx(ProceedingJoinPoint pjp) {
        log.info("wxRequest around start ");
        boolean flag = false;
        String methodName = pjp.getSignature().getName();
        Object[] objects = pjp.getArgs();
        String param = Arrays.stream(objects).map(n -> n == null ? null : n.toString()).collect(Collectors.joining(","));
        long start = System.currentTimeMillis();
        Object result = null;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//            Map<String, String> headersInfo = HttpServletRequestUtil.getHeaderMap(request);
//            log.info("wx_request_header: appId={}, envVersion={}, version={}", headersInfo.get("appid"), headersInfo.get("envversion"), headersInfo.get("version"));
//
//            String accessToken = headersInfo.get(WxAppletTokenUtil.ACCESS_TOKEN);
//            if (StringUtil.isNotBlank(accessToken)) {
//                WxAppletUserSessionBean userSession = userSessionService.findUserSession(accessToken);
//                if (userSession != null) {
//                    //设置userSession
//                    WxAppletRequestContext.getContext().setUserSession(userSession);
//                    log.info("wx_request_user_session: {}", JsonUtil.toJson(userSession));
//
//                    RegisterUserModel registerUser = null;
//                    if (StringUtil.isNotBlank(userSession.getRegisterUserId())) {
//                        registerUser = registerUserService.findById(userSession.getRegisterUserId());
//                    } else if (userSession.getChannelId() != null) {
//                        registerUser = registerUserService.findByChannelId(userSession.getChannelId(), userSession.getOpenid());
//                    }
//                    if (registerUser != null) {
//                        //设置user
//                        WxAppletRequestContext.getContext().setUser(registerUser);
//                        log.info("wx_request_user: {}", JsonUtil.toJson(registerUser));
//                    }
//                }
//            }

            log.info("wxRequest around prepare into method ");
            result = pjp.proceed();
            long end = System.currentTimeMillis();
//            EagleeyeLogConverter.setCost(end - start);
            log.info("wxRequest around out of method ");
//            EagleeyeLogConverter.setFlag(false);
            flag = true;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
//            EagleeyeLogConverter.setFlag(false);
            long end = System.currentTimeMillis();
//            EagleeyeLogConverter.setCost(end - start);
//            HttpResult httpResult;
//            if (throwable instanceof BaseException) {
//                BaseException baseException = (BaseException) throwable;
//                Integer code = ((BaseException) throwable).getCode();
//                httpResult = HttpResult.withError(code.toString(), baseException.getMessage());
//            } else {
//                httpResult = HttpResult.withError("400", "SYS_ERROR");
//                log.error("SYS_ERROR", throwable.getMessage() + EagleEye.getTraceId(), throwable);
//            }
//            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).setAttribute("ERROR_INFO",httpResult,0);
//            result = null;
//            log.info("|third|{}|weixin| execute exception,class: {}, method|{}, param: {}, result|{}",
//                    flag, pjp.getTarget().getClass().getName(), methodName, param, JsonUtil.toJson(httpResult));
        } finally {
            WxAppletRequestContext.remove();
        }
//        EagleeyeLogConverter.setFlag(flag);
//        log.info("|third|{}|weixin| execute success,class: {}, method|{}, param: {}, result|{}",
//                flag, pjp.getTarget().getClass().getName(), methodName, param, JsonUtil.toJson(result));
        return result;
    }
}
