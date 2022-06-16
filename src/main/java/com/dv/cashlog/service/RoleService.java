package com.dv.cashlog.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.dv.cashlog.api.response.Notification;
import com.dv.cashlog.common.dto.RoleDto;

public interface RoleService {
    RoleDto createRole(RoleDto roleReq, HttpServletRequest req);

    RoleDto getRole(long id);

    List<RoleDto> getRoles(int page, int limit);

    RoleDto updateRole(RoleDto roleReq);

    Notification deleteRole(long id);

    List<RoleDto> importFromExcel(List<MultipartFile> multipartFiles);

}
