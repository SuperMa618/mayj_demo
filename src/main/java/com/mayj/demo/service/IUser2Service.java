package com.mayj.demo.service;

import com.mayj.demo.model.base.User1;
import com.mayj.demo.model.base.User2;

/**
 * @InterfaceName IUserService
 * @Description
 * @Author Mayj
 * @Date 2022/3/12 15:05
 **/
public interface IUser2Service {

    void add2Required(User2 user);

    void add2RequiredException(User2 user);

    void addRequiresNew(User2 user);

    void addRequiresNewException(User2 user);

}
