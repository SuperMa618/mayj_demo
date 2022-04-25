package com.mayj.demo.service.Impl;

import com.mayj.demo.mapper.User1Mapper;
import com.mayj.demo.mapper.User2Mapper;
import com.mayj.demo.model.base.User1;
import com.mayj.demo.model.base.User2;
import com.mayj.demo.service.IUser1Service;
import com.mayj.demo.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author Mayj
 * @Date 2022/3/12 15:05
 **/
@Service
public class User1ServiceImpl implements IUser1Service {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private User1Mapper user1Mapper;
    @Resource
    private RedisUtil redisUtil;


    @Override
    public void add() {
        User1 user = new User1();
        user.setName("mayj");
        logger.info("user"+user.toString());
        user1Mapper.insert(user);
        redisUtil.set("hjhj", "m,yj");
        String a = redisUtil.get("hjhj").toString();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void add1Required(User1 user1) {
        user1Mapper.insert(user1);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRequiresNew(User1 user){
        user1Mapper.insert(user);
    }
}
