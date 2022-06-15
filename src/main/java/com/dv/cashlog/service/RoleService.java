package com.dv.cashlog.service;

import com.dv.cashlog.common.dto.RoleDto;

public interface RoleService {
    RoleDto createRole(RoleDto roleReq);
    RoleDto getRole(long id);
    RoleDto updateRole(RoleDto roleReq);
    boolean deleteRole(long id);
    
}
