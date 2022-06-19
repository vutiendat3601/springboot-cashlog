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
import com.dv.cashlog.common.dto.BankDto;
import com.dv.cashlog.exception.AppException;
import com.dv.cashlog.exception.ErrorMessage;
import com.dv.cashlog.io.entity.BankEntity;
import com.dv.cashlog.io.repository.BankRepository;
import com.dv.cashlog.service.BankService;

@Service
public class BankServiceImpl implements BankService {
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private BankRepository bankRepository;

    @Override
    public BankDto createBank(BankDto bankReq, HttpServletRequest req) {
        // Check existence of bank
        BankEntity bankEntity = bankRepository.findByName(bankReq.getName());
        if (bankEntity != null) {
            throw new AppException(ErrorMessage.NAME_WAS_EXISTED.getMessage(), HttpStatus.CONFLICT);
        }

        // Prepare data
        bankReq.setName(bankReq.getName().toUpperCase());
        // bankReq.setCreatedBy(req.getHeader("User-Name"));
        bankReq.setCreatedDate(LocalDateTime.now());
        // bankReq.setUpdatedBy(req.getHeader("User-Name"));
        bankReq.setUpdatedDate(LocalDateTime.now());
        bankReq.setIsDeleted(Boolean.FALSE);

        // Convert to Entity
        bankEntity = modelMapper.map(bankReq, BankEntity.class);

        try {
            // Save to Database
            bankEntity = bankRepository.save(bankEntity);

            // return DtoResponse
            BankDto bankResp = modelMapper.map(bankEntity, BankDto.class);
            return bankResp;
        } catch (Exception e) { // truong hop rot internet ....
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public BankDto getBank(long id, HttpServletRequest req) {
        // Check existence of bank
        Optional<BankEntity> bankRecord = bankRepository.findById(id);
        if (!bankRecord.isPresent()) {
            throw new AppException(ErrorMessage.NO_RECORD_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        try {
            // convert and return DtoRespone
            BankDto bankResp = modelMapper.map(bankRecord.get(), BankDto.class);
            return bankResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<BankDto> getBanks(int page, int limit, HttpServletRequest req) {
        // Check validation of param
        page = page > 0 ? page - 1 : 0;

        try {
            // Prepare data to get from Databse
            Pageable pageableReq = PageRequest.of(page, limit);
            Page<BankEntity> bankPage = bankRepository.findAll(pageableReq);
            List<BankEntity> bankRecords = bankPage.getContent();

            bankRecords = bankRecords.stream()
                    .filter(r -> !r.getIsDeleted())
                    .collect(Collectors.toList());

            if (bankRecords.isEmpty()) {
                throw new AppException(ErrorMessage.NO_RECORD_FOUND.getMessage(),
                        HttpStatus.NOT_FOUND);
            }

            // Convert and return DtoResp List
            List<BankDto> banksResp = new ArrayList<>();
            for (BankEntity bank : bankRecords) {
                BankDto bankResp = modelMapper.map(bank, BankDto.class);
                banksResp.add(bankResp);
            }
            return banksResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public BankDto updateBank(BankDto bankReq, HttpServletRequest req) {
        // Check exsitence of role
        Optional<BankEntity> bankRecord = bankRepository.findById(bankReq.getId());
        if (!bankRecord.isPresent()) {
            throw new AppException(ErrorMessage.NO_RECORD_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        try {
            // Convert DtoRequest to EntityRequest and prepare data
            bankReq.setId(bankRecord.get().getId());
            bankReq.setCreatedDate(bankRecord.get().getCreatedDate());
            bankReq.setCreatedBy(bankRecord.get().getCreatedBy());
            bankReq.setUpdatedDate(LocalDateTime.now());
            bankReq.setUpdatedBy(bankRecord.get().getUpdatedBy());
            bankReq.setIsDeleted(Boolean.FALSE);

            // Save to Database
            BankEntity updateBank = modelMapper.map(bankReq, BankEntity.class);
            updateBank = bankRepository.save(updateBank);

            // Convert and return DtoResp
            BankDto bankResp = modelMapper.map(updateBank, BankDto.class);
            return bankResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public NotificationResponse deleteBank(long id, HttpServletRequest req) {
        // Check existence of role
        Optional<BankEntity> bankRecord = bankRepository.findById(id);
        if (!bankRecord.isPresent()) {
            throw new AppException(ErrorMessage.NO_RECORD_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }
        try {
            // Delete role in Database and return Status
            bankRecord.get().setIsDeleted(Boolean.TRUE);
            bankRepository.save(bankRecord.get());
            return new NotificationResponse(LocalDateTime.now(),
                    NotificationStatus.SUCCESSFUL.getMessage());
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<BankDto> importFromExcel(List<MultipartFile> excelFiles, HttpServletRequest req) {
        // Check existence of file
        if (excelFiles.isEmpty()) {
            throw new AppException(ErrorMessage.FILE_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        // Prepare data to save
        List<BankEntity> bankRecords = new ArrayList<>();
        for (MultipartFile m : excelFiles) {
            try {
                XSSFWorkbook workBook = new XSSFWorkbook(m.getInputStream());
                XSSFSheet sheet = workBook.getSheetAt(0);

                // Read data row by row
                for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                    XSSFRow row = sheet.getRow(r);
                    BankDto bankReq = new BankDto();
                    bankReq.setName(row.getCell(0).getStringCellValue());

                    // Check existence of role
                    BankEntity bankEntity = bankRepository.findByName(bankReq.getName());
                    if (bankEntity != null) {
                        continue;
                    }

                    bankReq.setDescription(row.getCell(1).getStringCellValue());
                    bankReq.setCreatedDate(LocalDateTime.now());
                    // bankReq.setCreatedBy("Dat Vu");
                    bankReq.setUpdatedDate(LocalDateTime.now());
                    // bankReq.setUpdatedBy("Dat Vu");
                    bankReq.setIsDeleted(Boolean.FALSE);

                    BankEntity bankRecord = modelMapper.map(bankReq, BankEntity.class);
                    bankRecords.add(bankRecord);
                }
            } catch (IOException e) {
                throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Save to Database
        if (!bankRecords.isEmpty()) {
            try {
                bankRepository.saveAll(bankRecords);
            } catch (Exception e) {
                throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Convert and return BankDto List
        List<BankDto> banksResp = new ArrayList<>();
        for (BankEntity bank : bankRecords) {
            BankDto bankResp = modelMapper.map(bank, BankDto.class);
            banksResp.add(bankResp);
        }
        return banksResp;
    }

}
