package com.dv.cashlog.service;

import java.util.List;

import com.dv.cashlog.common.dto.UserDto;

public interface UserSerivce {

    UserDto createUser(UserDto userReq);

    UserDto getUser(String userCode);

    UserDto updateUser(UserDto userReq);

    List<UserDto> getUsers(int page, int limit);

    boolean deleteUser(String userCode);
}
