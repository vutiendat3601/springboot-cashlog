package com.dv.cashlog.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dv.cashlog.api.response.NotificationResponse;
import com.dv.cashlog.api.response.NotificationStatus;
import com.dv.cashlog.common.dto.ClassDto;
import com.dv.cashlog.common.dto.RoleDto;
import com.dv.cashlog.common.dto.UserDto;
import com.dv.cashlog.exception.AppException;
import com.dv.cashlog.exception.ErrorMessage;
import com.dv.cashlog.io.entity.ClassEntity;
import com.dv.cashlog.io.entity.RoleEntity;
import com.dv.cashlog.io.entity.UserEntity;
import com.dv.cashlog.io.repository.ClassRepository;
import com.dv.cashlog.io.repository.RoleRepository;
import com.dv.cashlog.io.repository.UserRepository;
import com.dv.cashlog.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClassRepository classRepository;

    @Override
    public UserDto createUser(UserDto userReq, HttpServletRequest req) {
        // Check existence of user email
        UserEntity userEntity = userRepository.findByEmail(userReq.getEmail());
        if (userEntity != null && !userEntity.getIsDeleted()) {
            throw new AppException(ErrorMessage.EMAIL_WAS_EXISTED.getMessage(),
                    HttpStatus.CONFLICT);
        }

        // Check existence of role
        RoleEntity roleEntity = roleRepository.findByName(userReq.getNameOfRole());
        if (roleEntity == null || roleEntity.getIsDeleted()) {
            throw new AppException(ErrorMessage.ROLE_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        // Prepare data
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        RoleDto role = modelMapper.map(roleEntity, RoleDto.class);
        userReq.setRole(role);
        ClassEntity classEntity = classRepository.findByName(userReq.getNameOfClass());
        ClassDto clazz = modelMapper.map(classEntity, ClassDto.class);
        userReq.setClazz(clazz);
        userReq.setCreatedDate(LocalDateTime.now());
        // userReq.setCreatedBy("Dat Vu");
        userReq.setUpdatedDate(LocalDateTime.now());
        // userReq.setUpdatedBy("Dat Vu");
        userReq.setIsDeleted(Boolean.FALSE);

        try {
            // Save to database
            userEntity = modelMapper.map(userReq, UserEntity.class);
            userEntity = userRepository.save(userEntity);

            // return DtoResponse
            UserDto userResp = modelMapper.map(userEntity, UserDto.class);
            return userResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UserDto getUser(String userCode, HttpServletRequest req) {
        // Check existence of user
        UserEntity userEntity = userRepository.findByUserCode(userCode);

        if (userEntity == null || userEntity.getIsDeleted()) {
            throw new AppException(ErrorMessage.USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        // convert and return DtoRespone
        try {
            UserDto userDtoResp = modelMapper.map(userEntity, UserDto.class);
            return userDtoResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<UserDto> getUsers(int page, int limit, HttpServletRequest req) {
        // Check validation of param
        page = page > 0 ? page - 1 : 0;

        try {
            // Prepare data to get from Databse
            Pageable pageableReq = PageRequest.of(page, limit);
            Page<UserEntity> userPage = userRepository.findAll(pageableReq);
            List<UserEntity> userRecords = userPage.getContent();

            userRecords = userRecords.stream()
                    .filter(r -> !r.getIsDeleted())
                    .collect(Collectors.toList());

            if (userRecords.isEmpty()) {
                throw new AppException(ErrorMessage.NO_RECORD_FOUND.getMessage(),
                        HttpStatus.NOT_FOUND);
            }

            // Convert and return DtoResp List
            List<UserDto> usersResp = new ArrayList<>();
            for (UserEntity user : userRecords) {
                UserDto userResp = modelMapper.map(user, UserDto.class);
                usersResp.add(userResp);
            }
            return usersResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UserDto updateUser(UserDto userReq, HttpServletRequest req) {
        // check existence of user
        UserEntity userEntity = userRepository.findByUserCode(userReq.getUserCode());

        if (userEntity == null || userEntity.getIsDeleted()) {
            throw new AppException(ErrorMessage.USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        ClassEntity classEntity = classRepository.findByName(userReq.getNameOfClass());
        if (classEntity == null || classEntity.getIsDeleted()) {
            throw new AppException(ErrorMessage.CLASS_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        // Prepare data
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        userReq.setId(userEntity.getId());
        userReq.setEmail(userEntity.getEmail());
        RoleDto role = modelMapper.map(userEntity.getRole(), RoleDto.class);
        userReq.setRole(role);
        ClassDto clazz = modelMapper.map(classEntity, ClassDto.class);
        userReq.setClazz(clazz);
        userReq.setCreatedDate(userEntity.getCreatedDate());
        userReq.setCreatedBy(userEntity.getCreatedBy());
        userReq.setUpdatedDate(LocalDateTime.now());
        userReq.setUpdatedBy(userEntity.getUpdatedBy());
        userReq.setIsDeleted(Boolean.FALSE);

        try {
            // Save to database
            UserEntity updateUser = modelMapper.map(userReq, UserEntity.class);
            updateUser = userRepository.save(updateUser);
            UserDto userResp = modelMapper.map(updateUser, UserDto.class);
            return userResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public NotificationResponse deleteUser(String userCode, HttpServletRequest req) {
        // Check existence of user
        UserEntity userEntity = userRepository.findByUserCode(userCode);
        if (userEntity == null || userEntity.getIsDeleted()) {
            throw new AppException(ErrorMessage.USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        try {
            // Save to database
            userEntity.setIsDeleted(Boolean.TRUE);
            userRepository.save(userEntity);
            return new NotificationResponse(LocalDateTime.now(),
                    NotificationStatus.SUCCESSFUL.getMessage());
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<UserDto> importFromExcel(List<MultipartFile> excelFiles, HttpServletRequest req) {
        // Check existence of file
        if (excelFiles.isEmpty()) {
            throw new AppException(ErrorMessage.FILE_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        // Prepare data to save
        List<UserEntity> userRecords = new ArrayList<>();
        for (MultipartFile m : excelFiles) {
            try {
                XSSFWorkbook workBook = new XSSFWorkbook(m.getInputStream());
                XSSFSheet sheet = workBook.getSheetAt(0);

                // Read data row by row
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT)
                        .setFullTypeMatchingRequired(true);
                for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                    XSSFRow row = sheet.getRow(r);
                    UserDto userReq = new UserDto();
                    userReq.setEmail(row.getCell(0).getStringCellValue()); // email

                    // Check existence of role
                    UserEntity userEntity = userRepository.findByEmail(userReq.getEmail());
                    if (userEntity != null && !userEntity.getIsDeleted()) {
                        continue;
                    }

                    userReq.setPassword(row.getCell(1).getStringCellValue());
                    userReq.setPhone(row.getCell(2).getStringCellValue());
                    userReq.setFullName(row.getCell(3).getStringCellValue());
                    userReq.setBirthday(row.getCell(4).getLocalDateTimeCellValue().toLocalDate());
                    userReq.setUserCode(row.getCell(5).getStringCellValue());
                    String roleName = row.getCell(6).getStringCellValue();

                    RoleEntity roleEntity = roleRepository.findByName(roleName);

                    if (roleEntity == null || roleEntity.getIsDeleted()) {
                        System.err.println(ErrorMessage.ROLE_NOT_FOUND.getMessage());
                        continue;
                    }

                    RoleDto role = modelMapper.map(roleEntity, RoleDto.class);
                    userReq.setRole(role);

                    userReq.setNameOfClass(row.getCell(7).getStringCellValue());

                    ClassEntity classEntity = classRepository.findByName(userReq.getNameOfClass());
                    ClassDto clazz = modelMapper.map(classEntity, ClassDto.class);
                    userReq.setClazz(clazz);

                    userReq.setCreatedDate(LocalDateTime.now());
                    // roleReq.setCreatedBy("Dat Vu");
                    userReq.setUpdatedDate(LocalDateTime.now());
                    // roleReq.setUpdatedBy("Dat Vu");
                    userReq.setIsDeleted(Boolean.FALSE);

                    UserEntity userRecord = modelMapper.map(userReq, UserEntity.class);
                    userRecords.add(userRecord);
                }
            } catch (IOException e) {
                throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Save to Database
        if (!userRecords.isEmpty()) {
            try {
                userRepository.saveAll(userRecords);
            } catch (Exception e) {
                throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Convert and return RoleDto List
        List<UserDto> usersResp = new ArrayList<>();
        for (UserEntity record : userRecords) {
            UserDto userDto = modelMapper.map(record, UserDto.class);
            usersResp.add(userDto);
        }
        return usersResp;
    }

}
