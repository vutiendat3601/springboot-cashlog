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
import com.dv.cashlog.common.dto.MajorDto;
import com.dv.cashlog.exception.AppException;
import com.dv.cashlog.exception.ErrorMessage;
import com.dv.cashlog.io.entity.ClassEntity;
import com.dv.cashlog.io.entity.MajorEntity;
import com.dv.cashlog.io.repository.ClassRepository;
import com.dv.cashlog.io.repository.MajorRepository;
import com.dv.cashlog.service.ClassService;

@Service
public class ClassServiceImpl implements ClassService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Override
    public ClassDto createClass(ClassDto classReq, HttpServletRequest req) {
        ClassEntity classEntity = classRepository.findByName(classReq.getName());

        // Check existence of class
        if (classEntity != null) {
            throw new AppException(ErrorMessage.CLASS_WAS_EXISTED.getMessage(), HttpStatus.CONFLICT);
        }

        // Check existence of major
        MajorEntity majorEntity = majorRepository.findByName(classReq.getNameOfMajor());
        if (majorEntity == null) {
            throw new AppException(ErrorMessage.MAJOR_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        MajorDto major = modelMapper.map(majorEntity, MajorDto.class);

        // Prepare data to save to database
        classReq.setMajor(major);

        // classReq.setCreatedBy("Dat Vu");
        classReq.setCreatedDate(LocalDateTime.now());
        // classReq.setUpdatedBy("Dat Vu");
        classReq.setUpdatedDate(LocalDateTime.now());
        classReq.setIsDeleted(Boolean.FALSE);

        // Save to database
        try {
            classEntity = modelMapper.map(classReq, ClassEntity.class);
            classEntity = classRepository.save(classEntity);

            ClassDto classDtoResp = modelMapper.map(classEntity, ClassDto.class);
            return classDtoResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ClassDto getClass(long id, HttpServletRequest req) {
        // Check existence of user
        Optional<ClassEntity> classEntity = classRepository.findById(id);

        if (!classEntity.isPresent()) {
            throw new AppException(ErrorMessage.CLASS_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        // convert and return DtoRespone
        try {
            ClassDto classDtoResp = modelMapper.map(classEntity.get(), ClassDto.class);
            return classDtoResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ClassDto> getClasses(int page, int limit, HttpServletRequest req) {
        // Check validation of param
        page = page > 0 ? page - 1 : 0;

        try {
            // Prepare data to get from Databse
            Pageable pageableReq = PageRequest.of(page, limit);
            Page<ClassEntity> classPage = classRepository.findAll(pageableReq);
            List<ClassEntity> classRecords = classPage.getContent();

            classRecords = classRecords.stream()
                    .filter(r -> !r.getIsDeleted())
                    .collect(Collectors.toList());

            if (classRecords.isEmpty()) {
                throw new AppException(ErrorMessage.NO_RECORD_FOUND.getMessage(),
                        HttpStatus.NOT_FOUND);
            }

            // Convert and return DtoResp List
            List<ClassDto> classesResp = new ArrayList<>();
            for (ClassEntity clazz : classRecords) {
                ClassDto classResp = modelMapper.map(clazz, ClassDto.class);
                classesResp.add(classResp);
            }
            return classesResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ClassDto updateClass(ClassDto classReq, HttpServletRequest req) {
        // Check exsitence of role
        Optional<ClassEntity> classRecord = classRepository.findById(classReq.getId());
        if (!classRecord.isPresent()) {
            throw new AppException(ErrorMessage.CLASS_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        // Check existence of major
        MajorEntity majorEntity = majorRepository.findByName(classReq.getNameOfMajor());
        if (majorEntity == null) {
            throw new AppException(ErrorMessage.MAJOR_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }
        try {
            // Convert DtoRequest to EntityRequest and prepare data
            classReq.setId(classRecord.get().getId());
            classReq.setCreatedDate(classRecord.get().getCreatedDate());
            classReq.setCreatedBy(classRecord.get().getCreatedBy());
            classReq.setUpdatedDate(LocalDateTime.now());
            classReq.setUpdatedBy(classRecord.get().getUpdatedBy());
            classReq.setIsDeleted(Boolean.FALSE);
            MajorDto major = modelMapper.map(majorEntity, MajorDto.class);
            classReq.setMajor(major);

            // Save to Database
            ClassEntity updateClass = modelMapper.map(classReq, ClassEntity.class);
            updateClass = classRepository.save(updateClass);

            // Convert and return DtoResp
            ClassDto classResp = modelMapper.map(updateClass, ClassDto.class);
            return classResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public NotificationResponse deleteClass(long id, HttpServletRequest req) {
        Optional<ClassEntity> classRecord = classRepository.findById(id);
        if (!classRecord.isPresent()) {
            throw new AppException(ErrorMessage.CLASS_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        try {
            // Delete class in Database and return Status
            classRecord.get().setIsDeleted(Boolean.TRUE);
            classRepository.save(classRecord.get());
            return new NotificationResponse(LocalDateTime.now(),
                    NotificationStatus.SUCCESSFUL.getMessage());
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ClassDto> importFromExcel(List<MultipartFile> excelFiles, HttpServletRequest req) {
        // Check existence of file
        if (excelFiles.isEmpty()) {
            throw new AppException(ErrorMessage.FILE_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        // Prepare data to save
        List<ClassEntity> classRecords = new ArrayList<>();
        for (MultipartFile m : excelFiles) {
            try {
                XSSFWorkbook workBook = new XSSFWorkbook(m.getInputStream());
                XSSFSheet sheet = workBook.getSheetAt(0);

                // Read data row by row
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT)
                        .setFullTypeMatchingRequired(true);
                for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                    XSSFRow row = sheet.getRow(r);
                    ClassDto classReq = new ClassDto();
                    classReq.setName(row.getCell(0).getStringCellValue().toUpperCase());

                    // Check existence of role
                    ClassEntity classEntity = classRepository.findByName(classReq.getName());
                    if (classEntity != null) {
                        continue;
                    }

                    classReq.setDescription(row.getCell(1).getStringCellValue());
                    classReq.setNameOfMajor(row.getCell(2).getStringCellValue());
                    MajorEntity majorEntity = majorRepository.findByName(classReq.getNameOfMajor());

                    if (majorEntity == null) {
                        System.err.println(ErrorMessage.MAJOR_NOT_FOUND.getMessage());
                        continue;
                    }

                    MajorDto major = modelMapper.map(majorEntity, MajorDto.class);
                    classReq.setMajor(major);
                    classReq.setCreatedDate(LocalDateTime.now());
                    // classReq.setCreatedBy("Dat Vu");
                    classReq.setUpdatedDate(LocalDateTime.now());
                    // classReq.setUpdatedBy("Dat Vu");
                    classReq.setIsDeleted(Boolean.FALSE);

                    ClassEntity classRecord = modelMapper.map(classReq, ClassEntity.class);
                    classRecords.add(classRecord);
                }
            } catch (IOException e) {
                throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Save to Database
        if (!classRecords.isEmpty()) {
            try {
                classRepository.saveAll(classRecords);
            } catch (Exception e) {
                throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Convert and return RoleDto List
        List<ClassDto> classesResp = new ArrayList<>();
        for (ClassEntity clazz : classRecords) {
            ClassDto classDto = modelMapper.map(clazz, ClassDto.class);
            classesResp.add(classDto);
        }
        return classesResp;
    }

}
