package com.dv.cashlog.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.dv.cashlog.api.response.NotificationResponse;
import com.dv.cashlog.api.response.NotificationStatus;
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
        // Check existence of role
        RoleEntity roleEntity = roleRepository.findByName(roleReq.getName());
        if (roleEntity != null) {
            throw new AppException(ErrorMessage.NAME_WAS_EXISTED.getMessage(), HttpStatus.CONFLICT);
        }

        // Prepare data
        roleReq.setName(roleReq.getName().toUpperCase());
        // roleReq.setCreatedBy(req.getHeader("User-Name"));
        roleReq.setCreatedDate(LocalDateTime.now());
        // roleReq.setUpdatedBy(req.getHeader("User-Name"));
        roleReq.setUpdatedDate(LocalDateTime.now());
        roleReq.setIsDeleted(Boolean.FALSE);

        // Convert to Entity
        roleEntity = modelMapper.map(roleReq, RoleEntity.class);

        try {
            // Save to Database
            roleEntity = roleRepository.save(roleEntity);

            // return DtoResponse
            RoleDto roleResp = modelMapper.map(roleEntity, RoleDto.class);
            return roleResp;
        } catch (Exception e) { // truong hop rot internet ....
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public RoleDto getRole(long id, HttpServletRequest req) {
        // Check existence of role
        Optional<RoleEntity> roleRecord = roleRepository.findById(id);
        if (!roleRecord.isPresent()) {
            throw new AppException(ErrorMessage.NO_RECORD_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        try {
            // convert and return DtoRespone
            RoleDto roleResp = modelMapper.map(roleRecord.get(), RoleDto.class);
            return roleResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<RoleDto> getRoles(int page, int limit, HttpServletRequest req) {

        // Check validation of param
        page = page > 0 ? page - 1 : 0;

        try {
            // Prepare data to get from Databse
            Pageable pageableReq = PageRequest.of(page, limit);
            Page<RoleEntity> rolePage = roleRepository.findAll(pageableReq);
            List<RoleEntity> roleRecords = rolePage.getContent();

            roleRecords = roleRecords.stream()
                    .filter(r -> !r.getIsDeleted())
                    .collect(Collectors.toList());

            if (roleRecords.isEmpty()) {
                throw new AppException(ErrorMessage.NO_RECORD_FOUND.getMessage(),
                        HttpStatus.NOT_FOUND);
            }

            // Convert and return DtoResp List
            List<RoleDto> rolesResp = new ArrayList<>();
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
    public RoleDto updateRole(RoleDto roleReq, HttpServletRequest req) {
        // Check exsitence of role
        Optional<RoleEntity> roleRecord = roleRepository.findById(roleReq.getId());
        if (!roleRecord.isPresent()) {
            throw new AppException(ErrorMessage.NO_RECORD_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        try {
            // Convert DtoRequest to EntityRequest and prepare data
            roleReq.setId(roleRecord.get().getId());
            roleReq.setCreatedDate(roleRecord.get().getCreatedDate());
            roleReq.setCreatedBy(roleRecord.get().getCreatedBy());
            roleReq.setUpdatedDate(LocalDateTime.now());
            roleReq.setUpdatedBy(roleRecord.get().getUpdatedBy());
            roleReq.setIsDeleted(Boolean.FALSE);

            // Save to Database
            RoleEntity updateRole = modelMapper.map(roleReq, RoleEntity.class);
            updateRole = roleRepository.save(updateRole);

            // Convert and return DtoResp
            RoleDto roleResp = modelMapper.map(updateRole, RoleDto.class);
            return roleResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public NotificationResponse deleteRole(long id, HttpServletRequest req) {
        // Check existence of role
        Optional<RoleEntity> roleRecord = roleRepository.findById(id);
        if (!roleRecord.isPresent()) {
            throw new AppException(ErrorMessage.NO_RECORD_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }
        try {
            // Delete role in Database and return Status
            roleRecord.get().setIsDeleted(Boolean.TRUE);
            roleRepository.save(roleRecord.get());
            return new NotificationResponse(LocalDateTime.now(),
                    NotificationStatus.SUCCESSFUL.getMessage());
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<RoleDto> importFromExcel(List<MultipartFile> excelFiles, HttpServletRequest req) {
        // Check existence of file
        if (excelFiles.isEmpty()) {
            throw new AppException(ErrorMessage.FILE_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        // Prepare data to save
        List<RoleEntity> roleRecords = new ArrayList<>();
        for (MultipartFile m : excelFiles) {
            try {
                XSSFWorkbook workBook = new XSSFWorkbook(m.getInputStream());
                XSSFSheet sheet = workBook.getSheetAt(0);

                // Read data row by row
                for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                    XSSFRow row = sheet.getRow(r);
                    RoleDto roleReq = new RoleDto();
                    roleReq.setName(row.getCell(0).getStringCellValue());

                    // Check existence of role
                    RoleEntity roleEntity = roleRepository.findByName(roleReq.getName());
                    if (roleEntity != null && !roleEntity.getIsDeleted()) {
                        continue;
                    }

                    roleReq.setDescription(row.getCell(1).getStringCellValue());
                    roleReq.setCreatedDate(LocalDateTime.now());
                    // roleReq.setCreatedBy("Dat Vu");
                    roleReq.setUpdatedDate(LocalDateTime.now());
                    // roleReq.setUpdatedBy("Dat Vu");
                    roleReq.setIsDeleted(Boolean.FALSE);

                    RoleEntity roleRecord = modelMapper.map(roleReq, RoleEntity.class);
                    roleRecords.add(roleRecord);
                }
            } catch (IOException e) {
                throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Save to Database
        if (!roleRecords.isEmpty()) {
            try {
                roleRepository.saveAll(roleRecords);
            } catch (Exception e) {
                throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Convert and return RoleDto List
        List<RoleDto> rolesResp = new ArrayList<>();
        for (RoleEntity record : roleRecords) {
            RoleDto roleDto = modelMapper.map(record, RoleDto.class);
            rolesResp.add(roleDto);
        }
        return rolesResp;
    }

}
