package com.example.demo.dao;

import com.example.demo.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<UserInfo, Long> {
    UserInfo findByUserName(String userName);
}
