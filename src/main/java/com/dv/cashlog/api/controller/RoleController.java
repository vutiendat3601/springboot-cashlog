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

import com.dv.cashlog.api.request.RoleRequest;
import com.dv.cashlog.api.response.NotificationResponse;
import com.dv.cashlog.api.response.RoleResponse;
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
    public RoleResponse createRole(@RequestBody @Validated RoleRequest roleReq, HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Convert Request Model to DtoRequest
        RoleDto roleDtoReq = modelMapper.map(roleReq, RoleDto.class);

        // Delegate to Service
        RoleDto roleDtoResp = roleService.createRole(roleDtoReq, req);

        // Convert DtoResponse to ResponseModel
        RoleResponse roleResp = modelMapper.map(roleDtoResp, RoleResponse.class);

        return roleResp;
    }

    @PostMapping("/import-from-excel")
    public List<RoleResponse> importFromExcel(
            @RequestParam("Source-File") List<MultipartFile> excelFiles,
            HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Delegate to Service
        List<RoleDto> rolesDtoResp = roleService.importFromExcel(excelFiles, req);

        // Convert DtoResponse to ResponseModel
        List<RoleResponse> rolesResp = new ArrayList<>();
        rolesDtoResp.forEach(e -> {
            RoleResponse roleElement = modelMapper.map(e, RoleResponse.class);
            rolesResp.add(roleElement);
        });

        return rolesResp;
    }

    @GetMapping("/get/{id}")
    public RoleResponse getRole(@PathVariable long id, HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Delegate to Service
        RoleDto roleDtoResp = roleService.getRole(id, req);

        // Convert DtoResponse to ResponseModel
        RoleResponse roleResp = modelMapper.map(roleDtoResp, RoleResponse.class);

        return roleResp;
    }

    @GetMapping("/get")
    public List<RoleResponse> getRoles(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            HttpServletRequest req) {
        log.info("HTTP Request: {}", req);


        
        // Delegate to Service
        List<RoleDto> rolesDtoResp = roleService.getRoles(page, limit, req);
        
        // Convert DtoResponse to ResponseModel
        List<RoleResponse> rolesResp = new ArrayList<>();
        for (RoleDto role : rolesDtoResp) {
            RoleResponse roleResp = modelMapper.map(role, RoleResponse.class);
            rolesResp.add(roleResp);
        }

        return rolesResp;
    }

    @PutMapping("/update/{id}")
    public RoleResponse updateRole(@PathVariable long id, @RequestBody RoleRequest roleReq,
            HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Convert RequestModel to DtoRequest
        RoleDto roleDtoReq = modelMapper.map(roleReq, RoleDto.class);
        roleDtoReq.setId(id);

        // Delegate to Service
        RoleDto roleDtoResp = roleService.updateRole(roleDtoReq, req);

        // Convert DtoResponse to ResponseModel
        RoleResponse roleResp = modelMapper.map(roleDtoResp, RoleResponse.class);

        return roleResp;
    }

    @DeleteMapping("/delete/{id}")
    public NotificationResponse deleteRole(@PathVariable long id, HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Delegate to Service and return Status
        return roleService.deleteRole(id, req);
    }

}
