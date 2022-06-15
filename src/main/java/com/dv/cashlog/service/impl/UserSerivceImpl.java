package com.dv.cashlog.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dv.cashlog.common.dto.UserDto;
import com.dv.cashlog.io.entity.RoleEntity;
import com.dv.cashlog.io.entity.UserEntity;
import com.dv.cashlog.io.repository.UserRepository;
import com.dv.cashlog.service.UserSerivce;

@Service
public class UserSerivceImpl implements UserSerivce {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userReq) {
        UserEntity userExistenceChecking = userRepository.findByEmail(userReq.getEmail());
        if (userExistenceChecking != null) {
            throw new RuntimeException("Email was taken!!!");
        }

        ModelMapper modelMapper = new ModelMapper();

        userReq.setEncryptedPassword("123456a@");

        UserEntity userEntityReq = modelMapper.map(userReq, UserEntity.class);
        UserEntity userEntityResp = userRepository.save(userEntityReq);
        UserDto userDtoResp = modelMapper.map(userEntityResp, UserDto.class);
        return userDtoResp;
    }

    @Override
    public UserDto getUser(String userCode) {
        UserEntity userEntityResp = userRepository.findByUserCode(userCode);
        if (userEntityResp == null) {
            throw new RuntimeException("No user was found!!!");
        }
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDtoResp = modelMapper.map(userEntityResp, UserDto.class);
        return userDtoResp;
    }

    @Override
    public UserDto updateUser(UserDto userReq) {

        UserEntity userEntityResp = userRepository.findByUserCode(userReq.getUserCode());
        if (userEntityResp == null) {
            throw new RuntimeException("No user was found!!!");
        }

        ModelMapper modelMapper = new ModelMapper();

        UserEntity userEntityReq = modelMapper.map(userReq, UserEntity.class);
        userEntityReq.setId(userEntityResp.getId());
        userEntityReq.setEncryptedPassword(userEntityResp.getEncryptedPassword());
        userEntityReq.setEmail(userEntityResp.getEmail());
        userEntityReq.setRole(userEntityResp.getRole());
        userEntityResp = userRepository.save(userEntityReq);
        UserDto userDtoResp = modelMapper.map(userEntityResp, UserDto.class);
        return userDtoResp;
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> usersDtoResp = new ArrayList<>();

        page = page > 0 ? page - 1 : 0;

        Pageable pageableReq = PageRequest.of(page, limit);

        Page<UserEntity> userPage = userRepository.findAll(pageableReq);
        List<UserEntity> users = userPage.getContent();

        ModelMapper modelMapper = new ModelMapper();
        for (UserEntity user : users) {
            UserDto userDtoResp = modelMapper.map(user, UserDto.class);
            usersDtoResp.add(userDtoResp);
        }

        return usersDtoResp;
    }

    @Override
    public boolean deleteUser(String userCode) {
        UserEntity userEntityResp = userRepository.findByUserCode(userCode);
        if (userEntityResp == null) {
            throw new RuntimeException("User was not found!!!");
        }
        userRepository.delete(userEntityResp);
        return true;
    }

}
