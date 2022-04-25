package com.mayj.demo.service.Impl;

import com.mayj.demo.mapper.User1Mapper;
import com.mayj.demo.mapper.User2Mapper;
import com.mayj.demo.model.base.User1;
import com.mayj.demo.model.base.User2;
import com.mayj.demo.service.IUser1Service;
import com.mayj.demo.service.IUser2Service;
import com.mayj.demo.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName UserServiceImpl
 * @Description
 * @Author Mayj
 * @Date 2022/3/12 15:05
 **/
@Service
public class User2ServiceImpl implements IUser2Service {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private User1Mapper user1Mapper;
    @Resource
    private User2Mapper user2Mapper;
    @Resource
    private RedisUtil redisUtil;

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void add2Required(User2 user){
        user2Mapper.insert(user);
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void add2RequiredException(User2 user){
        user2Mapper.insert(user);
        throw new RuntimeException();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public void addRequiresNew(User2 user){
        user2Mapper.insert(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRequiresNewException(User2 user){
        user2Mapper.insert(user);
        throw new RuntimeException();
    }
}
