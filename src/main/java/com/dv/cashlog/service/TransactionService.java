package com.dv.cashlog.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.dv.cashlog.common.dto.TransactionDto;

public interface TransactionService {
    TransactionDto createTransaction(TransactionDto transactionReq, HttpServletRequest req);

    List<TransactionDto> getTransactions(int page, int limit, HttpServletRequest req);

}
