package com.dv.cashlog.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dv.cashlog.common.dto.RoleDto;
import com.dv.cashlog.io.entity.RoleEntity;
import com.dv.cashlog.io.repository.RoleRepository;
import com.dv.cashlog.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleDto createRole(RoleDto roleReq) {
        RoleEntity roleExstenceChecking = roleRepository.findByName(roleReq.getName());
        if (roleExstenceChecking != null) {
            throw new RuntimeException("Role was existed!!!");
        }

        ModelMapper modelMapper = new ModelMapper();

        RoleEntity roleEntityReq = modelMapper.map(roleReq, RoleEntity.class);
        RoleEntity roleEntityResp = roleRepository.save(roleEntityReq);

        RoleDto roleDtoResp = modelMapper.map(roleEntityResp, RoleDto.class);
        return roleDtoResp;
    }

    @Override
    public RoleDto getRole(long id) {
        Optional<RoleEntity> roleExistenceChecking = roleRepository.findById(id);
        if (!roleExistenceChecking.isPresent()) {
            throw new RuntimeException("Role was not found!!!");
        }
        ModelMapper modelMapper = new ModelMapper();
        RoleDto roleDtoResp = modelMapper.map(roleExistenceChecking.get(), RoleDto.class);
        return roleDtoResp;
    }

    @Override
    public List<RoleDto> getRoles(int page, int limit) {
        List<RoleDto> rolesDtoResp = new ArrayList<>();

        page = page > 0 ? page - 1 : page;

        Pageable pageableReq = PageRequest.of(page, limit);

        Page<RoleEntity> rolePage = roleRepository.findAll(pageableReq);
        List<RoleEntity> roles = rolePage.getContent();

        ModelMapper modelMapper = new ModelMapper();
        for (RoleEntity role : roles) {
            RoleDto roleDtoResp = modelMapper.map(role, RoleDto.class);
            rolesDtoResp.add(roleDtoResp);
        }

        return rolesDtoResp;
    }

    @Override
    public RoleDto updateRole(RoleDto roleReq) {
        Optional<RoleEntity> roleExistenceChecking = roleRepository.findById(roleReq.getId());
        if (!roleExistenceChecking.isPresent()) {
            throw new RuntimeException("Role was not found!!!");
        }
        ModelMapper modelMapper = new ModelMapper();
        RoleEntity roleEntityReq = modelMapper.map(roleReq, RoleEntity.class);
        RoleEntity roleEntityResp = roleRepository.save(roleEntityReq);
        RoleDto roleDtoResp = modelMapper.map(roleEntityResp, RoleDto.class);
        return roleDtoResp;
    }

    @Override
    public boolean deleteRole(long id) {
        Optional<RoleEntity> roleExistenceChecking = roleRepository.findById(id);
        if (!roleExistenceChecking.isPresent()) {
            throw new RuntimeException("Role was not found!!!");
        }
        roleRepository.delete(roleExistenceChecking.get());
        return true;
    }

}
