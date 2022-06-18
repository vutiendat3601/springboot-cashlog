package com.dv.cashlog.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.multipart.MultipartFile;

import com.dv.cashlog.api.request.UserRequest;
import com.dv.cashlog.api.request.UserUpdateRequest;
import com.dv.cashlog.api.response.NotificationResponse;
import com.dv.cashlog.api.response.RoleResponse;
import com.dv.cashlog.api.response.UserResponse;
import com.dv.cashlog.common.dto.UserDto;
import com.dv.cashlog.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/user")
public class UserController {

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public UserResponse createUser(@RequestBody UserRequest userReq, HttpServletRequest req) {
        log.info("HTTP Request: {}", req);
        // Convert RequestModel to DtoRequest
        UserDto userDtoReq = modelMapper.map(userReq, UserDto.class);

        // Delegate to Service
        UserDto userDtoResp = userService.createUser(userDtoReq, req);

        // Convert DtoResponse to ResponseModel
        RoleResponse role = modelMapper.map(userDtoResp.getRole(), RoleResponse.class);
        UserResponse userResp = modelMapper.map(userDtoResp, UserResponse.class);
        userResp.setRole(role);
        return userResp;
    }

    @PostMapping("/create/import-from-excel")
    public List<UserResponse> importFromExcel(
            @RequestParam("Source-File") List<MultipartFile> excelFiles,
            HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Delegate to Service
        List<UserDto> usersDtoResp = userService.importFromExcel(excelFiles, req);

        // Convert DtoResponse to ResponseModel
        List<UserResponse> usersResp = new ArrayList<>();
        usersDtoResp.forEach(e -> {
            UserResponse userResponse = modelMapper.map(e, UserResponse.class);
            usersResp.add(userResponse);
        });

        return usersResp;
    }

    @GetMapping("/get/{userCode}")
    public UserResponse getUser(@PathVariable String userCode, HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Delegate to Service
        UserDto userDtoResp = userService.getUser(userCode, req);

        // Convert DtoResponse to ResponseModel
        RoleResponse role = modelMapper.map(userDtoResp.getRole(), RoleResponse.class);
        UserResponse userResp = modelMapper.map(userDtoResp, UserResponse.class);
        userResp.setRole(role);
        return userResp;
    }

    @GetMapping("/get")
    public List<UserResponse> getUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Delegate to Service
        List<UserDto> usersDtoResp = userService.getUsers(page, limit, req);

        // Convert DtoResponse to ResponseModel
        List<UserResponse> usersResp = new ArrayList<>();
        for (UserDto userDto : usersDtoResp) {
            RoleResponse role = modelMapper.map(userDto.getRole(), RoleResponse.class);
            UserResponse userResponse = modelMapper.map(userDto, UserResponse.class);
            userResponse.setRole(role);
            usersResp.add(userResponse);
        }
        return usersResp;
    }

    @PutMapping("update/{userCode}")
    public UserResponse updateUser(@PathVariable String userCode,
            @RequestBody UserUpdateRequest userReq,
            HttpServletRequest req) {
        // Convert to UserDto
        UserDto userDtoReq = modelMapper.map(userReq, UserDto.class);
        userDtoReq.setUserCode(userCode);

        // Delegate to Service
        UserDto userDtoResp = userService.updateUser(userDtoReq, req);

        // Convert UserDtoResponse to UserResponse and return
        UserResponse userResp = modelMapper.map(userDtoResp, UserResponse.class);
        return userResp;
    }

    @DeleteMapping("/delete/{userCode}")
    public NotificationResponse deleteUser(@PathVariable String userCode, HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        return userService.deleteUser(userCode, req);
    }
}
