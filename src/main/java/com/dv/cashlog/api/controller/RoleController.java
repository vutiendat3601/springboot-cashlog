package com.dv.cashlog.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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

import com.dv.cashlog.api.request.RoleRequestModel;
import com.dv.cashlog.api.response.Notification;
import com.dv.cashlog.api.response.RoleResponseModel;
import com.dv.cashlog.common.dto.RoleDto;
import com.dv.cashlog.service.RoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/role")
public class RoleController {
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    public RoleResponseModel createRole(@RequestBody @Validated RoleRequestModel roleReq, HttpServletRequest req) {
        log.info("HTTP Request: {}", req);
        RoleDto roleDtoReq = modelMapper.map(roleReq, RoleDto.class);
        RoleDto roleDtoResp = roleService.createRole(roleDtoReq, req);
        RoleResponseModel roleResp = modelMapper.map(roleDtoResp, RoleResponseModel.class);
        return roleResp;
    }

    @PostMapping("/import-from-excel")
    public List<RoleResponseModel> importFromExcel(
            @RequestParam("Source-File") List<MultipartFile> files, HttpServletRequest req) {
        log.info("HTTP Request: {}", req);
        List<RoleDto> roleDtoResp = roleService.importFromExcel(files);
        List<RoleResponseModel> rolesResp = new ArrayList<>();
        roleDtoResp.forEach(o -> {
            RoleResponseModel roleResp = modelMapper.map(o, RoleResponseModel.class);
            rolesResp.add(roleResp);
        });
        return rolesResp;
    }

    @GetMapping("/get/{id}")
    public RoleResponseModel getRole(@PathVariable long id, HttpServletRequest req) {
        log.info("HTTP Request: {}", req);
        ModelMapper modelMapper = new ModelMapper();
        RoleDto roleDtoResp = roleService.getRole(id);
        RoleResponseModel roleResp = modelMapper.map(roleDtoResp, RoleResponseModel.class);
        return roleResp;
    }

    @GetMapping("/get")
    public List<RoleResponseModel> getRoles(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            HttpServletRequest req) {
        log.info("HTTP Request: {}", req);
        List<RoleResponseModel> rolesResp = new ArrayList<>();
        List<RoleDto> rolesDtoResp = roleService.getRoles(page, limit);

        ModelMapper modelMapper = new ModelMapper();
        for (RoleDto role : rolesDtoResp) {
            RoleResponseModel roleResp = modelMapper.map(role, RoleResponseModel.class);
            rolesResp.add(roleResp);
        }
        return rolesResp;
    }

    @PutMapping("/update/{id}")
    public RoleResponseModel updateRole(@PathVariable long id, @RequestBody RoleRequestModel roleReq,
            HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        ModelMapper modelMapper = new ModelMapper();

        RoleDto roleDtoReq = modelMapper.map(roleReq, RoleDto.class);
        roleDtoReq.setId(id);

        RoleDto roleDtoResp = roleService.updateRole(roleDtoReq);
        RoleResponseModel roleResp = modelMapper.map(roleDtoResp, RoleResponseModel.class);
        return roleResp;
    }

    @DeleteMapping("/delete/{id}")
    public Notification deleteRole(@PathVariable long id, HttpServletRequest req) {
        log.info("HTTP Request: {}", req);
        return roleService.deleteRole(id);
    }

}
