package com.dv.cashlog.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dv.cashlog.api.request.BankRequest;
import com.dv.cashlog.api.request.RoleRequest;
import com.dv.cashlog.api.response.BankResponse;
import com.dv.cashlog.api.response.NotificationResponse;
import com.dv.cashlog.common.dto.BankDto;
import com.dv.cashlog.service.BankService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("v1/bank")
public class BankController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BankService bankService;

    @PostMapping("/create")
    public BankResponse createBank(@RequestBody @Validated BankRequest bankReq, HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Convert Request Model to DtoRequest
        BankDto bankDtoReq = modelMapper.map(bankReq, BankDto.class);

        // Delegate to Service
        BankDto bankDtoResp = bankService.createBank(bankDtoReq, req);

        // Convert DtoResponse to ResponseModel
        BankResponse bankResp = modelMapper.map(bankDtoResp, BankResponse.class);

        return bankResp;
    }

    @PostMapping("/create/import-from-excel")
    public List<BankResponse> importFromExcel(
            @RequestParam("Source-File") List<MultipartFile> excelFiles,
            HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Delegate to Service
        List<BankDto> banksDtoReq = bankService.importFromExcel(excelFiles, req);

        // Convert DtoResponse to ResponseModel
        List<BankResponse> banksResp = new ArrayList<>();
        banksDtoReq.forEach(e -> {
            BankResponse bankResp = modelMapper.map(e, BankResponse.class);
            banksResp.add(bankResp);
        });

        return banksResp;
    }

    @GetMapping("/get/{id}")
    public BankResponse getBank(@PathVariable long id, HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Delegate to Service
        BankDto bankDtoResp = bankService.getBank(id, req);

        // Convert DtoResponse to ResponseModel
        BankResponse bankResp = modelMapper.map(bankDtoResp, BankResponse.class);

        return bankResp;
    }

    @GetMapping("/get")
    public List<BankResponse> getBanks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Delegate to Service
        List<BankDto> banksDtoResp = bankService.getBanks(page, limit, req);

        // Convert DtoResponse to ResponseModel
        List<BankResponse> banksResp = new ArrayList<>();
        for (BankDto bank : banksDtoResp) {
            BankResponse bankResp = modelMapper.map(bank, BankResponse.class);
            banksResp.add(bankResp);
        }

        return banksResp;
    }

    @PutMapping("/update/{id}")
    public BankResponse updateBank(@PathVariable long id, @RequestBody @Validated BankRequest bankReq,
            HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Convert RequestModel to DtoRequest
        BankDto bankDtoReq = modelMapper.map(bankReq, BankDto.class);
        bankDtoReq.setId(id);

        // Delegate to Service
        BankDto bankDtoResp = bankService.updateBank(bankDtoReq, req);

        // Convert DtoResponse to ResponseModel
        BankResponse bankResp = modelMapper.map(bankDtoResp, BankResponse.class);

        return bankResp;
    }

    @DeleteMapping("/delete/{id}")
    public NotificationResponse deleteBank(@PathVariable long id, HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Delegate to Service and return Status
        return bankService.deleteBank(id, req);
    }

}
