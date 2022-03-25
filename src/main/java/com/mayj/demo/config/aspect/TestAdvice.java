package com.mayj.demo.config.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @ClassName TestAdvice
 * @Description
 * @Author Mayj
 * @Date 2022/3/25 23:05
 **/
@Aspect
@Component
public class TestAdvice {

    @Pointcut("execution(public * com.mayj.demo.service.Impl.UserServiceImpl.*(..))")
    private void userHandle(){}

    @Around("userHandle()")
    public void handlerRpc(ProceedingJoinPoint point) throws Throwable {
        System.out.println("add before");
        point.proceed();
        System.out.println("add after");
    }
}
