package com.dv.cashlog.service;

import java.util.List;

import com.dv.cashlog.common.dto.MajorDto;

public interface MajorService {

    MajorDto createMajor(MajorDto majorDtoReq);

    MajorDto getMajor(long id);

    List<MajorDto> getMajors(int page, int limit);
    
}
