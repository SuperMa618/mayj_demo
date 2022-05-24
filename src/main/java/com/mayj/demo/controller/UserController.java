package com.mayj.demo.controller;

import com.mayj.demo.model.base.User1;
import com.mayj.demo.model.base.User2;
import com.mayj.demo.service.IUser1Service;
import com.mayj.demo.service.IUser2Service;
import com.mayj.demo.task.ChildTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
    private IUser1Service user1Service;
    @Resource
    private IUser2Service user2Service;

    @ApiOperation("this is test api")
    @PostMapping("handle")
    public String add(@ApiParam(value = "id") @RequestParam(required = false) String id) {
        user1Service.add();
        return id;
    }


    /*
    结论：
    以上试验结果我们证明在外围方法开启事务的情况下
    Propagation.REQUIRED修饰的内部方法会加入到外围方法的事务中，
    所有Propagation.REQUIRED修饰的内部方法和外围方法均属于同一事务，
    只要一个方法回滚，整个事务均回滚。
     */
    @ApiOperation("验证事务传播性1")
    @PostMapping("transaction1")
    public void transaction1() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.add1Required(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.add2Required(user2);

        throw new RuntimeException();
        //return "yes";
    }

    @ApiOperation("验证事务传播性2")
    @PostMapping("transaction2")
    public void transaction2() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.add1Required(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.add2RequiredException(user2);
    }

    @ApiOperation("验证事务传播性3")
    @PostMapping("transaction3")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void transaction3() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.add1Required(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        try {
            user2Service.add2RequiredException(user2);
        } catch (Exception e) {
            System.out.println("方法回滚");
        }
    }


/*
在外围方法开启事务的情况下
Propagation.REQUIRES_NEW修饰的内部方法依然会单独开启独立事务，
且与外部方法事务也独立，内部方法之间、内部方法和外部方法事务均相互独立，互不干扰。
 */

    @ApiOperation("验证事务传播性4")
    @PostMapping("transaction4")
    public void transaction4() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.add1Required(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.add2RequiredException(user2);
    }


    ChildTask c = new ChildTask("myj");
    @ApiOperation("启动多线程任务")
    @PostMapping("startThreadTask")
    public void startThreadTask() {
        c.doExecute();
    }

    @ApiOperation("停止多线程任务")
    @PostMapping("stopThreadTask")
    public void stopThreadTask() {
        try {
            c.terminal();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
