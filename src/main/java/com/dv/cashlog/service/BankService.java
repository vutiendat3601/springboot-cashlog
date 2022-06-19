package com.dv.cashlog.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.dv.cashlog.api.response.NotificationResponse;
import com.dv.cashlog.common.dto.BankDto;

public interface BankService {
    BankDto createBank(BankDto bankReq, HttpServletRequest req);

    BankDto getBank(long id, HttpServletRequest req);

    List<BankDto> getBanks(int page, int limit, HttpServletRequest req);

    BankDto updateBank(BankDto roleReq, HttpServletRequest req);

    NotificationResponse deleteBank(long id, HttpServletRequest req);

    List<BankDto> importFromExcel(List<MultipartFile> excelFiles, HttpServletRequest req);
}
