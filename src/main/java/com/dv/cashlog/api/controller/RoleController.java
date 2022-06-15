package com.dv.cashlog.api.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dv.cashlog.api.request.RoleRequestModel;
import com.dv.cashlog.api.response.RoleResponseModel;
import com.dv.cashlog.common.dto.RoleDto;
import com.dv.cashlog.service.RoleService;

@RestController
@RequestMapping("/v1/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    public RoleResponseModel createRole(@RequestBody RoleRequestModel roleReq) {
        ModelMapper modelMapper = new ModelMapper();
        RoleDto roleDtoReq = modelMapper.map(roleReq, RoleDto.class);

        RoleDto roleDtoResp = roleService.createRole(roleDtoReq);
        RoleResponseModel roleResp = modelMapper.map(roleDtoResp, RoleResponseModel.class);
        return roleResp;
    }

    @GetMapping("/get/{id}")
    public RoleResponseModel getRole(@PathVariable long id) {
        ModelMapper modelMapper = new ModelMapper();

        RoleDto roleDtoResp = roleService.getRole(id);
        RoleResponseModel roleResp = modelMapper.map(roleDtoResp, RoleResponseModel.class);
        return roleResp;
    }

    @PutMapping("/update/{id}")
    public RoleResponseModel updateRole(@PathVariable long id, @RequestBody RoleRequestModel roleReq) {
        ModelMapper modelMapper = new ModelMapper();

        RoleDto roleDtoReq = modelMapper.map(roleReq, RoleDto.class);
        roleDtoReq.setId(id);

        RoleDto roleDtoResp = roleService.updateRole(roleDtoReq);
        RoleResponseModel roleResp = modelMapper.map(roleDtoResp, RoleResponseModel.class);
        return roleResp;
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteRole(@PathVariable long id) {
        boolean res = roleService.deleteRole(id);
        return res;
    }

}
