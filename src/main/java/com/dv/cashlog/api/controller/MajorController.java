package com.dv.cashlog.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.dv.cashlog.api.request.MajorRequest;
import com.dv.cashlog.api.response.MajorResponse;
import com.dv.cashlog.api.response.NotificationResponse;
import com.dv.cashlog.common.dto.MajorDto;
import com.dv.cashlog.service.MajorService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/major")
public class MajorController {

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private MajorService majorService;

    @PostMapping("/create")
    public MajorResponse createMajor(@RequestBody MajorRequest majorReq, HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Convert Request Model to DtoRequest
        MajorDto majorDtoReq = modelMapper.map(majorReq, MajorDto.class);

        // Delegate to Service
        MajorDto majorDtoResp = majorService.createMajor(majorDtoReq, req);

        // Convert DtoResponse to ResponseModel
        MajorResponse majorResp = modelMapper.map(majorDtoResp, MajorResponse.class);

        return majorResp;
    }

    @PostMapping("/create/import-from-excel")
    public List<MajorResponse> importFromExcel(
            @RequestParam("Source-File") List<MultipartFile> excelFiles,
            HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Delegate to Service
        List<MajorDto> majorsDtoResp = majorService.importFromExcel(excelFiles, req);

        // Convert DtoResponse to ResponseModel
        List<MajorResponse> majorsResp = new ArrayList<>();
        majorsDtoResp.forEach(e -> {
            MajorResponse majorResp = modelMapper.map(e, MajorResponse.class);
            majorsResp.add(majorResp);
        });

        return majorsResp;
    }

    @GetMapping("/get/{id}")
    public MajorResponse getMajor(@PathVariable long id, HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Delegate to Service
        MajorDto majorDtoResp = majorService.getMajor(id, req);

        // Convert DtoResponse to ResponseModel
        MajorResponse majorResp = modelMapper.map(majorDtoResp, MajorResponse.class);

        return majorResp;
    }

    @GetMapping("/get")
    public List<MajorResponse> getMajors(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Delegate to Service
        List<MajorDto> majorsDtoResp = majorService.getMajors(page, limit, req);

        // Convert DtoResponse to ResponseModel
        List<MajorResponse> majorsResp = new ArrayList<>();
        for (MajorDto major : majorsDtoResp) {
            MajorResponse majorResp = modelMapper.map(major, MajorResponse.class);
            majorsResp.add(majorResp);
        }

        return majorsResp;
    }

    @PutMapping("/update/{id}")
    public MajorResponse updateMajor(@PathVariable long id, @RequestBody MajorRequest majorReq,
            HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Convert RequestModel to DtoRequest
        MajorDto majorDtoReq = modelMapper.map(majorReq, MajorDto.class);
        majorDtoReq.setId(id);

        // Delegate to Service
        MajorDto majorDtoResp = majorService.updateMajor(majorDtoReq, req);

        // Convert DtoResponse to ResponseModel
        MajorResponse majorResp = modelMapper.map(majorDtoResp, MajorResponse.class);

        return majorResp;
    }

    @DeleteMapping("/delete/{id}")
    public NotificationResponse deleteMajor(@PathVariable long id, HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Delegate to Service and return Status
        return majorService.deleteMajor(id, req);
    }

}
