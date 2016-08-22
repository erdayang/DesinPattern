package com.jd.si.person.service;

import com.jd.si.person.user.thrift.User;
import com.jd.si.person.user.thrift.UserService;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

/**
 * Created by yangxianda on 2016/8/22.
 */
public class UserServiceImpl implements UserService.Iface {

    @Override
    public User get() throws TException {

        User user = new User();
        user.setAge(33);
        user.setName("yangxianda");
        return user;
    }
}
