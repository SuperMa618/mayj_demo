package com.mayj.demo.controller;

import com.mayj.demo.model.base.User1;
import com.mayj.demo.model.base.User2;
import com.mayj.demo.rabbitmq.producer.Sender;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName RabbitMqController
 * @Description 测试消息队列
 * @Author Mayj
 * @Date 2022/3/12 15:04
 **/
@Api("MQ")
@RestController
@RequestMapping("mq")
public class RabbitMqController {

    @Resource
    private Sender sender;

    @ApiOperation("this is test api")
    @PostMapping("handle")
    public String add(@ApiParam(value = "id") @RequestParam(required = false) String id) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(id + i+"*********");
            sender.send(list);
        }
        return id;
    }

}
