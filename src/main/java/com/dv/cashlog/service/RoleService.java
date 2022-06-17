package com.dv.cashlog.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.dv.cashlog.api.response.NotificationResponse;
import com.dv.cashlog.common.dto.RoleDto;

public interface RoleService {
    RoleDto createRole(RoleDto roleReq, HttpServletRequest req);

    RoleDto getRole(long id, HttpServletRequest req);

    List<RoleDto> getRoles(int page, int limit, HttpServletRequest req);

    RoleDto updateRole(RoleDto roleReq, HttpServletRequest req);

    NotificationResponse deleteRole(long id, HttpServletRequest req);

    List<RoleDto> importFromExcel(List<MultipartFile> excelFiles, HttpServletRequest req);

}
