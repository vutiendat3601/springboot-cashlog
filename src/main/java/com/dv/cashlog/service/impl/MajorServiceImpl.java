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
import com.dv.cashlog.common.dto.MajorDto;
import com.dv.cashlog.exception.AppException;
import com.dv.cashlog.exception.ErrorMessage;
import com.dv.cashlog.io.entity.MajorEntity;
import com.dv.cashlog.io.repository.MajorRepository;
import com.dv.cashlog.service.MajorService;

@Service
public class MajorServiceImpl implements MajorService {

    @Autowired
    private MajorRepository majorRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public MajorDto createMajor(MajorDto majorReq, HttpServletRequest req) {
        // Check existence of role
        MajorEntity majorEntity = majorRepository.findByName(majorReq.getName());
        if (majorEntity != null && !majorEntity.getIsDeleted()) {
            throw new AppException(ErrorMessage.NAME_WAS_EXISTED.getMessage(), HttpStatus.CONFLICT);
        }

        // Prepare data
        majorReq.setName(majorReq.getName().toUpperCase());
        // majorReq.setCreatedBy(req.getHeader("User-Name"));
        majorReq.setCreatedDate(LocalDateTime.now());
        // majorReq.setUpdatedBy(req.getHeader("User-Name"));
        majorReq.setUpdatedDate(LocalDateTime.now());
        majorReq.setIsDeleted(Boolean.FALSE);

        // Convert to Entity
        majorEntity = modelMapper.map(majorReq, MajorEntity.class);

        try {
            // Save to Database
            majorEntity = majorRepository.save(majorEntity);

            // return DtoResponse
            MajorDto majorResp = modelMapper.map(majorEntity, MajorDto.class);
            return majorResp;
        } catch (Exception e) { // truong hop rot internet ....
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public MajorDto getMajor(long id, HttpServletRequest req) {
        // Check existence of role
        Optional<MajorEntity> majorRecord = majorRepository.findById(id);
        if (!majorRecord.isPresent() || majorRecord.get().getIsDeleted()) {
            throw new AppException(ErrorMessage.NO_RECORD_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        try {
            // convert and return DtoRespone
            MajorDto majorResp = modelMapper.map(majorRecord.get(), MajorDto.class);
            return majorResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<MajorDto> getMajors(int page, int limit, HttpServletRequest req) {
        // Check validation of param
        page = page > 0 ? page - 1 : 0;

        try {
            // Prepare data to get from Databse
            Pageable pageableReq = PageRequest.of(page, limit);
            Page<MajorEntity> majorPage = majorRepository.findAll(pageableReq);
            List<MajorEntity> majorRecords = majorPage.getContent();

            majorRecords = majorRecords.stream()
                    .filter(r -> !r.getIsDeleted())
                    .collect(Collectors.toList());

            if (majorRecords.isEmpty()) {
                throw new AppException(ErrorMessage.NO_RECORD_FOUND.getMessage(),
                        HttpStatus.NOT_FOUND);
            }

            // Convert and return DtoResp List
            List<MajorDto> majorsResp = new ArrayList<>();
            for (MajorEntity major : majorRecords) {
                MajorDto majorResp = modelMapper.map(major, MajorDto.class);
                majorsResp.add(majorResp);
            }
            return majorsResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public MajorDto updateMajor(MajorDto majorReq, HttpServletRequest req) {
        // Check exsitence of role
        Optional<MajorEntity> majorRecord = majorRepository.findById(majorReq.getId());
        if (!majorRecord.isPresent() || majorRecord.get().getIsDeleted()) {
            throw new AppException(ErrorMessage.NO_RECORD_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        try {
            // Convert DtoRequest to EntityRequest and prepare data
            majorReq.setId(majorRecord.get().getId());
            majorReq.setCreatedDate(majorRecord.get().getCreatedDate());
            majorReq.setCreatedBy(majorRecord.get().getCreatedBy());
            majorReq.setUpdatedDate(LocalDateTime.now());
            majorReq.setUpdatedBy(majorRecord.get().getUpdatedBy());
            majorReq.setIsDeleted(Boolean.FALSE);

            // Save to Database
            MajorEntity updateMajor = modelMapper.map(majorReq, MajorEntity.class);
            updateMajor = majorRepository.save(updateMajor);

            // Convert and return DtoResp
            MajorDto majorResp = modelMapper.map(updateMajor, MajorDto.class);
            return majorResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public NotificationResponse deleteMajor(long id, HttpServletRequest req) {
        // Check existence of role
        Optional<MajorEntity> majorRecord = majorRepository.findById(id);
        if (!majorRecord.isPresent()) {
            throw new AppException(ErrorMessage.NO_RECORD_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }
        try {
            // Delete role in Database and return Status
            majorRecord.get().setIsDeleted(Boolean.TRUE);
            majorRepository.save(majorRecord.get());
            return new NotificationResponse(LocalDateTime.now(),
                    NotificationStatus.SUCCESSFUL.getMessage());
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<MajorDto> importFromExcel(List<MultipartFile> excelFiles, HttpServletRequest req) {
        // Check existence of file
        if (excelFiles.isEmpty()) {
            throw new AppException(ErrorMessage.FILE_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        // Prepare data to save
        List<MajorEntity> majorRecords = new ArrayList<>();
        for (MultipartFile m : excelFiles) {
            try {
                XSSFWorkbook workBook = new XSSFWorkbook(m.getInputStream());
                XSSFSheet sheet = workBook.getSheetAt(0);

                // Read data row by row
                for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                    XSSFRow row = sheet.getRow(r);
                    MajorDto majorReq = new MajorDto();
                    majorReq.setName(row.getCell(0).getStringCellValue());

                    // Check existence of role
                    MajorEntity MajorEntity = majorRepository.findByName(majorReq.getName());
                    if (MajorEntity != null && !MajorEntity.getIsDeleted()) {
                        continue;
                    }

                    majorReq.setDescription(row.getCell(1).getStringCellValue());
                    majorReq.setCreatedDate(LocalDateTime.now());
                    // majorReq.setCreatedBy("Dat Vu");
                    majorReq.setUpdatedDate(LocalDateTime.now());
                    // majorReq.setUpdatedBy("Dat Vu");
                    majorReq.setIsDeleted(Boolean.FALSE);

                    MajorEntity majorRecord = modelMapper.map(majorReq, MajorEntity.class);
                    majorRecords.add(majorRecord);
                }
            } catch (IOException e) {
                throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Save to Database
        if (!majorRecords.isEmpty()) {
            try {
                majorRepository.saveAll(majorRecords);
            } catch (Exception e) {
                throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Convert and return MajorDto List
        List<MajorDto> majorsResp = new ArrayList<>();
        for (MajorEntity major : majorRecords) {
            MajorDto majorDtoResp = modelMapper.map(major, MajorDto.class);
            majorsResp.add(majorDtoResp);
        }
        return majorsResp;
    }
}
