package com.dv.cashlog.service;

import java.util.List;

import com.dv.cashlog.common.dto.RoleDto;

public interface RoleService {
    RoleDto createRole(RoleDto roleReq);

    RoleDto getRole(long id);

    List<RoleDto> getRoles(int page, int limit);

    RoleDto updateRole(RoleDto roleReq);

    boolean deleteRole(long id);

}
