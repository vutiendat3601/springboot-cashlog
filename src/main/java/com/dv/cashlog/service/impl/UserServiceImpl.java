package com.dv.cashlog.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dv.cashlog.api.response.NotificationResponse;
import com.dv.cashlog.api.response.NotificationStatus;
import com.dv.cashlog.common.dto.RoleDto;
import com.dv.cashlog.common.dto.UserDto;
import com.dv.cashlog.exception.AppException;
import com.dv.cashlog.exception.ErrorMessage;
import com.dv.cashlog.io.entity.RoleEntity;
import com.dv.cashlog.io.entity.UserEntity;
import com.dv.cashlog.io.repository.RoleRepository;
import com.dv.cashlog.io.repository.UserRepository;
import com.dv.cashlog.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDto createUser(UserDto userReq, HttpServletRequest req) {
        // Check existence of user email
        UserEntity userEntity = userRepository.findByEmail(userReq.getEmail());
        if (userEntity != null && !userEntity.getIsDeleted()) {
            throw new AppException(ErrorMessage.EMAIL_WAS_EXISTED.getMessage(),
                    HttpStatus.CONFLICT);
        }

        // Check existence of role
        RoleEntity roleEntity = roleRepository.findByName(userReq.getRoleName());
        if (roleEntity == null || roleEntity.getIsDeleted()) {
            throw new AppException(ErrorMessage.ROLE_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        // Prepare data
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        RoleDto role = modelMapper.map(roleEntity, RoleDto.class);
        userReq.setRole(role);
        userReq.setCreatedDate(LocalDateTime.now());
        // userReq.setCreatedBy("Dat Vu");
        userReq.setUpdatedDate(LocalDateTime.now());
        // userReq.setUpdatedBy("Dat Vu");
        userReq.setIsDeleted(Boolean.FALSE);

        try {
            // Save to database
            userEntity = modelMapper.map(userReq, UserEntity.class);
            userEntity = userRepository.save(userEntity);

            // return DtoResponse
            UserDto userResp = modelMapper.map(userEntity, UserDto.class);
            return userResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UserDto getUser(String userCode, HttpServletRequest req) {
        // Check existence of user
        UserEntity userEntity = userRepository.findByUserCode(userCode);

        if (userEntity == null) {
            throw new AppException(ErrorMessage.ROLE_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        // convert and return DtoRespone
        try {
            UserDto userDtoResp = modelMapper.map(userEntity, UserDto.class);
            return userDtoResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<UserDto> getUsers(int page, int limit, HttpServletRequest req) {
        // Check validation of param
        page = page > 0 ? page - 1 : 0;

        try {
            // Prepare data to get from Databse
            Pageable pageableReq = PageRequest.of(page, limit);
            Page<UserEntity> userPage = userRepository.findAll(pageableReq);
            List<UserEntity> userRecords = userPage.getContent();

            userRecords = userRecords.stream()
                    .filter(r -> !r.getIsDeleted())
                    .collect(Collectors.toList());

            if (userRecords.isEmpty()) {
                throw new AppException(ErrorMessage.NO_RECORD_FOUND.getMessage(),
                        HttpStatus.NOT_FOUND);
            }

            // Convert and return DtoResp List
            List<UserDto> usersResp = new ArrayList<>();
            for (UserEntity user : userRecords) {
                UserDto userResp = modelMapper.map(user, UserDto.class);
                usersResp.add(userResp);
            }
            return usersResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UserDto updateUser(UserDto userReq, HttpServletRequest req) {
        // check existence of user
        UserEntity userEntity = userRepository.findByUserCode(userReq.getUserCode());

        if (userEntity == null || userEntity.getIsDeleted()) {
            throw new AppException(ErrorMessage.USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        // Prepare data
        RoleDto role = modelMapper.map(userEntity.getRole(), RoleDto.class);
        userReq.setRole(role);
        userReq.setCreatedDate(userEntity.getCreatedDate());
        userReq.setCreatedBy(userEntity.getCreatedBy());
        userReq.setUpdatedDate(LocalDateTime.now());
        userReq.setUpdatedBy(userEntity.getUpdatedBy());
        userReq.setIsDeleted(Boolean.FALSE);

        try {
            // Save to database
            UserEntity updateUser = modelMapper.map(userReq, UserEntity.class);
            updateUser = userRepository.save(updateUser);
            UserDto userResp = modelMapper.map(updateUser, UserDto.class);
            return userResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public NotificationResponse deleteUser(String userCode, HttpServletRequest req) {
        // Check existence of user
        UserEntity userEntity = userRepository.findByUserCode(userCode);
        if (userEntity == null || userEntity.getIsDeleted()) {
            throw new AppException(ErrorMessage.USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        try {
            // Save to database
            userEntity.setIsDeleted(Boolean.TRUE);
            userRepository.save(userEntity);
            return new NotificationResponse(LocalDateTime.now(),
                    NotificationStatus.SUCCESSFUL.getMessage());
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
