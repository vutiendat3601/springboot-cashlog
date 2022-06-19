package com.dv.cashlog.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dv.cashlog.api.request.TransactionRequest;
import com.dv.cashlog.api.response.TransactionResponse;
import com.dv.cashlog.common.dto.TransactionDto;
import com.dv.cashlog.service.TransactionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/transaction")
public class TransactionController {

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/create")
    public TransactionResponse createTransaction(
            @RequestBody @Validated TransactionRequest transactionReq,
            HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Convert to Dto Request
        TransactionDto transactionDtoReq = modelMapper.map(transactionReq, TransactionDto.class);

        // Delegating to Service
        TransactionDto transactionDtoResp = transactionService.createTransaction(transactionDtoReq, req);

        // Convert and return Response
        TransactionResponse transactionResp = modelMapper.map(transactionDtoResp, TransactionResponse.class);
        return transactionResp;
    }

    @GetMapping("/get")
    public List<TransactionResponse> getTransactions(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            HttpServletRequest req) {
        log.info("HTTP Request: {}", req);

        // Delegate to Service
        List<TransactionDto> transactionsDtoResp = transactionService.getTransactions(page, limit, req);

        // Convert DtoResponse to ResponseModel
        List<TransactionResponse> transactionsResp = new ArrayList<>();
        for (TransactionDto transaction : transactionsDtoResp) {
            TransactionResponse transactionResp = modelMapper.map(transaction, TransactionResponse.class);
            transactionsResp.add(transactionResp);
        }
        return transactionsResp;
    }
}
