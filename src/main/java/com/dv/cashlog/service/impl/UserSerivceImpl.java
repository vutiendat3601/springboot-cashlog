package com.dv.cashlog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dv.cashlog.io.entity.UserEntity;
import com.dv.cashlog.io.repository.UserRepository;
import com.dv.cashlog.service.UserSerivce;

@Service
public class UserSerivceImpl implements UserSerivce {

    @Autowired
    private UserRepository userRepository;

    @Override
    public String createUser() {
        return "Hello World";
    }

    @Override
    public String getUser(String userCode) {
        UserEntity userEntityResp = userRepository.findByUserCode(userCode);
        return userEntityResp.getEncryptedPassword();
    }

}
