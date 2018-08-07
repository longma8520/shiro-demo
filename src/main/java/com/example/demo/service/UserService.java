package com.example.demo.service;

import com.example.demo.dao.UserDao;
import com.example.demo.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public Boolean checkUser(String userName, String password){
        UserInfo userInfo = userDao.findByUserName(userName);
        if(userInfo == null){
            return false;
        }
        if(password.equals(userInfo.getPassword())){
            return true;
        }
        return false;
    }
}
