package com.dv.cashlog.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.dv.cashlog.api.response.NotificationResponse;
import com.dv.cashlog.common.dto.MajorDto;

public interface MajorService {
    MajorDto createMajor(MajorDto userReq, HttpServletRequest req);

    MajorDto getMajor(long id, HttpServletRequest req);

    List<MajorDto> getMajors(int page, int limit, HttpServletRequest req);

    MajorDto updateMajor(MajorDto majorReq, HttpServletRequest req);

    NotificationResponse deleteMajor(long id, HttpServletRequest req);

    List<MajorDto> importFromExcel(List<MultipartFile> excelFiles, HttpServletRequest req);
}
