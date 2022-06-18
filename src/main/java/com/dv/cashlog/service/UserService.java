package com.dv.cashlog.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.dv.cashlog.api.response.NotificationResponse;
import com.dv.cashlog.common.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto userReq, HttpServletRequest req);

    UserDto getUser(String userCode, HttpServletRequest req);

    List<UserDto> getUsers(int page, int limit, HttpServletRequest req);

    UserDto updateUser(UserDto userReq, HttpServletRequest req);

    NotificationResponse deleteUser(String userCode, HttpServletRequest req);

    List<UserDto> importFromExcel(List<MultipartFile> excelFiles, HttpServletRequest req);
}
