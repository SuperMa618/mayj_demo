package com.mayj.demo.service.Impl;

import com.mayj.demo.mapper.UserMapper;
import com.mayj.demo.model.base.User;
import com.mayj.demo.service.IUserService;
import com.mayj.demo.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author Mayj
 * @Date 2022/3/12 15:05
 **/
@Service
public class UserServiceImpl implements IUserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisUtil redisUtil;


    @Override
    public void add() {
        User user = new User();
        user.setName("mayj");
        logger.info("user"+user.toString());
        userMapper.insert(user);
        redisUtil.set("hjhj", "m,yj");
        String a = redisUtil.get("hjhj").toString();
    }
}
