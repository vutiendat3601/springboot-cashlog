package com.dv.cashlog.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.dv.cashlog.common.dto.TransactionDto;

public interface TransactionService {
    TransactionDto createTransaction(TransactionDto transactionReq, HttpServletRequest req);

    List<TransactionDto> getTransactions(int page, int limit, HttpServletRequest req);

    List<TransactionDto> importFromExcel(List<MultipartFile> excelFiles, HttpServletRequest req);
}
