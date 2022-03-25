package com.mayj.demo.config.aspect;

import org.aspectj.lang.annotation.Pointcut;

/**
 * @ClassName Pointuts
 * @Description
 * @Author Mayj
 * @Date 2022/3/25 23:23
 **/
public class Pointuts {

    @Pointcut("execution(* com.mayj.demo.controller.UserController.*(..))")
    public void log(){}

    @Pointcut("execution(* com.mayj.demo.controller.UserController.*(..))")
    public void error(){}
}
