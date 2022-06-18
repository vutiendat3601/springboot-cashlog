package com.dv.cashlog.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.dv.cashlog.api.response.NotificationResponse;
import com.dv.cashlog.common.dto.ClassDto;

public interface ClassService {
    ClassDto createClass(ClassDto classReq, HttpServletRequest req);

    ClassDto getClass(long id, HttpServletRequest req);

    List<ClassDto> getClasses(int page, int limit, HttpServletRequest req);

    ClassDto updateClass(ClassDto classReq, HttpServletRequest req);

    NotificationResponse deleteClass(long id, HttpServletRequest req);

    List<ClassDto> importFromExcel(List<MultipartFile> excelFiles, HttpServletRequest req);
}
