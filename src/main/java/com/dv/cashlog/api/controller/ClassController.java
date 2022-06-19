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

import com.dv.cashlog.api.request.ClassRequest;
import com.dv.cashlog.api.response.ClassResponse;
import com.dv.cashlog.api.response.NotificationResponse;
import com.dv.cashlog.common.dto.ClassDto;
import com.dv.cashlog.service.ClassService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/class")
public class ClassController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ClassService classService;

    @PostMapping("/create")
    public ClassResponse createClass(@RequestBody @Validated ClassRequest classReq, HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Convert Request Model to DtoRequest
        ClassDto classDtoReq = modelMapper.map(classReq, ClassDto.class);

        // Delegate to Service
        ClassDto classDtoResp = classService.createClass(classDtoReq, req);

        // Convert DtoResponse to ResponseModel
        ClassResponse classResp = modelMapper.map(classDtoResp, ClassResponse.class);

        return classResp;
    }

    @PostMapping("/create/import-from-excel")
    public List<ClassResponse> importFromExcel(
            @RequestParam("Source-File") List<MultipartFile> excelFiles,
            HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Delegate to Service
        List<ClassDto> classesDtoResp = classService.importFromExcel(excelFiles, req);

        // Convert DtoResponse to ResponseModel
        List<ClassResponse> classesResp = new ArrayList<>();
        classesDtoResp.forEach(e -> {
            ClassResponse classResp = modelMapper.map(e, ClassResponse.class);
            classesResp.add(classResp);
        });

        return classesResp;
    }

    @GetMapping("/get/{id}")
    public ClassResponse getClass(@PathVariable long id, HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Delegate to Service
        ClassDto classDtoResp = classService.getClass(id, req);

        // Convert DtoResponse to ResponseModel
        ClassResponse classResp = modelMapper.map(classDtoResp, ClassResponse.class);

        return classResp;
    }

    @GetMapping("/get")
    public List<ClassResponse> getClasses(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Delegate to Service
        List<ClassDto> classesDtoResp = classService.getClasses(page, limit, req);

        // Convert DtoResponse to ResponseModel
        List<ClassResponse> majorsResp = new ArrayList<>();
        for (ClassDto clazz : classesDtoResp) {
            ClassResponse classResp = modelMapper.map(clazz, ClassResponse.class);
            majorsResp.add(classResp);
        }
        return majorsResp;
    }

    @PutMapping("/update/{id}")
    public ClassResponse updateClass(@PathVariable long id, @RequestBody @Validated ClassRequest classReq,
            HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Convert RequestModel to DtoRequest
        ClassDto classDtoReq = modelMapper.map(classReq, ClassDto.class);
        classDtoReq.setId(id);

        // Delegate to Service
        ClassDto classDtoResp = classService.updateClass(classDtoReq, req);

        // Convert DtoResponse to ResponseModel
        ClassResponse classResp = modelMapper.map(classDtoResp, ClassResponse.class);

        return classResp;
    }

    @DeleteMapping("/delete/{id}")
    public NotificationResponse deleteClass(@PathVariable long id, HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Delegate to Service and return Status
        return classService.deleteClass(id, req);
    }
}
