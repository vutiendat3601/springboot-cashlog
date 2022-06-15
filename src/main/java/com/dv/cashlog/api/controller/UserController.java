package com.dv.cashlog.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dv.cashlog.api.request.UserRequestModel;
import com.dv.cashlog.api.request.UserUpdateRequestModel;
import com.dv.cashlog.api.response.UserResponseModel;
import com.dv.cashlog.common.dto.RoleDto;
import com.dv.cashlog.common.dto.UserDto;
import com.dv.cashlog.service.RoleService;
import com.dv.cashlog.service.UserSerivce;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserSerivce userSerivce;

    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    public UserResponseModel createUser(@RequestBody UserRequestModel userReq) {
        ModelMapper modelMapper = new ModelMapper();

        RoleDto roleDto = roleService.getRole(userReq.getRoleId());
        UserDto userDtoReq = modelMapper.map(userReq, UserDto.class);
        userDtoReq.setRole(roleDto);
        UserDto userDtoResp = userSerivce.createUser(userDtoReq);

        UserResponseModel userResp = modelMapper.map(userDtoResp, UserResponseModel.class);
        return userResp;
    }

    @GetMapping("/get/{userCode}")
    public UserResponseModel getUser(@PathVariable String userCode) {
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDtoResp = userSerivce.getUser(userCode);
        UserResponseModel userResp = modelMapper.map(userDtoResp, UserResponseModel.class);
        return userResp;
    }

    @GetMapping("get/all")
    public List<UserResponseModel> getUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<UserResponseModel> usersResp = new ArrayList<>();

        List<UserDto> usersDtoResp = userSerivce.getUsers(page, limit);

        ModelMapper modelMapper = new ModelMapper();
        for (UserDto userDto : usersDtoResp) {
            UserResponseModel userResp = modelMapper.map(userDto, UserResponseModel.class);
            usersResp.add(userResp);
        }
        return usersResp;
    }

    @PutMapping("/update/{userCode}")
    public UserResponseModel upateUser(
            @PathVariable String userCode,
            @RequestBody UserUpdateRequestModel userReq) {

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDtoReq = modelMapper.map(userReq, UserDto.class);
        userDtoReq.setUserCode(userCode);
        UserDto userDtoResp = userSerivce.updateUser(userDtoReq);
        UserResponseModel userResp = modelMapper.map(userDtoResp, UserResponseModel.class);
        return userResp;
    }

    @DeleteMapping("/delete/{userCode}")
    public boolean deleteUser(@PathVariable String userCode) {
        return userSerivce.deleteUser(userCode);
    }

}
