package com.dv.cashlog.service.impl;

import org.springframework.stereotype.Service;

import com.dv.cashlog.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Override
    public String createTransaction() {
        return "created transaction";
    }

}
