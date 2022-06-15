package com.dv.cashlog.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dv.cashlog.service.UserSerivce;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserSerivce userSerivce;

    @PostMapping("/create")
    public String createUser() {
        return userSerivce.createUser();
    }

    @GetMapping("/get/{userCode}")
    public String getUser(@PathVariable String userCode) {
        return userSerivce.getUser(userCode);
    }

}
