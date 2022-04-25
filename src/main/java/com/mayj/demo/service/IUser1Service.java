package com.mayj.demo.service;

import com.mayj.demo.model.base.User1;
import com.mayj.demo.model.base.User2;

/**
 * @InterfaceName IUserService
 * @Description TODO
 * @Author Mayj
 * @Date 2022/3/12 15:05
 **/
public interface IUser1Service {

    void add();

    void add1Required(User1 user1);

    void addRequiresNew(User1 user);
}
