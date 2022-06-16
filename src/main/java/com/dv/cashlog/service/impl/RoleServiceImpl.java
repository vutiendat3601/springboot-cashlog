package com.dv.cashlog.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dv.cashlog.api.response.Notification;
import com.dv.cashlog.api.response.NotificationMessage;
import com.dv.cashlog.common.dto.RoleDto;
import com.dv.cashlog.exception.AppException;
import com.dv.cashlog.exception.ErrorMessage;
import com.dv.cashlog.io.entity.RoleEntity;
import com.dv.cashlog.io.repository.RoleRepository;
import com.dv.cashlog.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleDto createRole(RoleDto roleReq, HttpServletRequest req) {
        RoleEntity roleEntity = roleRepository.findByName(roleReq.getName());

        if (roleEntity != null) {
            throw new AppException(ErrorMessage.NAME_WAS_EXISTED.getMessage(), HttpStatus.CONFLICT);
        }

        roleReq.setName(roleReq.getName().toUpperCase());

        roleReq.setCreatedBy(req.getHeader("User-Name"));
        roleReq.setCreatedDate(LocalDateTime.now());
        roleReq.setUpdatedBy(req.getHeader("User-Name"));
        roleReq.setUpdatedDate(LocalDateTime.now());
        roleReq.setIsDeleted(Boolean.FALSE);

        roleEntity = modelMapper.map(roleReq, RoleEntity.class);
        try {
            roleEntity = roleRepository.save(roleEntity);
            RoleDto roleResp = modelMapper.map(roleEntity, RoleDto.class);
            return roleResp;
        } catch (Exception e) { // truong hop rot internet ....
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public RoleDto getRole(long id) {
        Optional<RoleEntity> roleRecord = roleRepository.findById(id);
        if (!roleRecord.isPresent()) {
            throw new AppException(ErrorMessage.NO_RECORD_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        try {
            RoleDto roleResp = modelMapper.map(roleRecord.get(), RoleDto.class);
            return roleResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<RoleDto> getRoles(int page, int limit) {

        page = page > 0 ? page - 1 : 0;

        List<RoleDto> rolesResp = new ArrayList<>();
        try {
            Pageable pageableReq = PageRequest.of(page, limit);
            Page<RoleEntity> rolePage = roleRepository.findAll(pageableReq);
            List<RoleEntity> roleRecords = rolePage.getContent();
            for (RoleEntity role : roleRecords) {
                RoleDto roleResp = modelMapper.map(role, RoleDto.class);
                rolesResp.add(roleResp);
            }
            return rolesResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public RoleDto updateRole(RoleDto roleReq) {
        Optional<RoleEntity> roleRecord = roleRepository.findById(roleReq.getId());

        if (!roleRecord.isPresent()) {
            throw new AppException(ErrorMessage.NO_RECORD_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        try {
            RoleEntity updateRole = modelMapper.map(roleReq, RoleEntity.class);

            updateRole.setId(roleRecord.get().getId());
            updateRole.setUpdatedDate(LocalDateTime.now());

            updateRole = roleRepository.save(updateRole);
            RoleDto roleResp = modelMapper.map(updateRole, RoleDto.class);
            return roleResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Notification deleteRole(long id) {
        Optional<RoleEntity> roleRecord = roleRepository.findById(id);
        if (!roleRecord.isPresent()) {
            throw new AppException(ErrorMessage.NO_RECORD_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        try {
            roleRepository.delete(roleRecord.get());
            return new Notification(LocalDateTime.now(),
                    NotificationMessage.SUCCESSFUL.getMessage());
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<RoleDto> importFromExcel(List<MultipartFile> multipartFiles) {
        if (multipartFiles.isEmpty()) {
            throw new AppException(ErrorMessage.FILE_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }
        List<RoleEntity> roleRecords = new ArrayList<>();
        for (MultipartFile m : multipartFiles) {
            try {
                XSSFWorkbook workBook = new XSSFWorkbook(m.getInputStream());
                XSSFSheet sheet = workBook.getSheetAt(0);
                for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                    XSSFRow row = sheet.getRow(r);
                    RoleDto roleReq = new RoleDto();
                    roleReq.setName(row.getCell(0).getStringCellValue());
                    roleReq.setDescription(row.getCell(1).getStringCellValue());
                    RoleEntity roleRecord = modelMapper.map(roleReq, RoleEntity.class);
                    roleRecords.add(roleRecord);
                }
            } catch (IOException e) {
                throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        if (!roleRecords.isEmpty()) {
            roleRepository.saveAll(roleRecords);
        }

        List<RoleDto> rolesResp = new ArrayList<>();
        for (RoleEntity record : roleRecords) {
            RoleDto roleDto = modelMapper.map(record, RoleDto.class);
            rolesResp.add(roleDto);
        }
        return rolesResp;
    }

}
