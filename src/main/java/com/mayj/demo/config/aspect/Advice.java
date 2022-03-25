package com.mayj.demo.config.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @ClassName Adivce
 * @Description
 * @Author Mayj
 * @Date 2022/3/25 23:25
 **/
@Aspect
public class Advice {
    @Before("com.mayj.demo.config.aspect.Pointuts.log()")
    public void before(JoinPoint joinPoint) {
        System.out.println("Logging before " + joinPoint.getSignature().getName());
    }
}
