package com.mayj.demo.controller;

import com.mayj.demo.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author Mayj
 * @Date 2022/3/12 15:04
 **/
@Api("User")
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private IUserService userService;

    @ApiOperation("this is test api")
    @PostMapping("handle")
    public String add(@ApiParam(value = "id") @RequestParam(required = false) String id) {
        userService.add();
        return id;
    }
}
