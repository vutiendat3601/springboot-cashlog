package com.dv.cashlog.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dv.cashlog.common.dto.BankDto;
import com.dv.cashlog.common.dto.TransactionDto;
import com.dv.cashlog.common.dto.UserDto;
import com.dv.cashlog.exception.AppException;
import com.dv.cashlog.exception.ErrorMessage;
import com.dv.cashlog.io.entity.BankEntity;
import com.dv.cashlog.io.entity.TransactionEntity;
import com.dv.cashlog.io.entity.UserEntity;
import com.dv.cashlog.io.repository.BankRepository;
import com.dv.cashlog.io.repository.TransactionRepository;
import com.dv.cashlog.io.repository.UserRepository;
import com.dv.cashlog.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankRepository bankRepository;

    @Override
    public TransactionDto createTransaction(TransactionDto transactionReq, HttpServletRequest req) {
        // Check collected/paid constraint
        if (transactionReq.getCollected() != 0 && transactionReq.getPaid() != 0) {
            throw new AppException(ErrorMessage.TRANSACTION_CONSTRAINT_ERROR.getMessage(), HttpStatus.BAD_REQUEST);
        }

        // Check existence of user email
        UserEntity userEntity = userRepository.findByUserCode(transactionReq.getUserCodeOfUser());
        if (userEntity == null) {
            throw new AppException(ErrorMessage.USER_NOT_FOUND.getMessage(),
                    HttpStatus.NOT_FOUND);
        }

        // Check existence of role
        BankEntity bankEntity = bankRepository.findByName(transactionReq.getNameOfBank());
        if (bankEntity == null) {
            throw new AppException(ErrorMessage.BANK_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }

        // Prepare data
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto user = modelMapper.map(userEntity, UserDto.class);
        transactionReq.setUser(user);
        BankDto bank = modelMapper.map(bankEntity, BankDto.class);
        transactionReq.setBank(bank);
        transactionReq.setDateTime(LocalDateTime.now());

        transactionReq.setCreatedDate(LocalDateTime.now());
        // userReq.setCreatedBy("Dat Vu");
        transactionReq.setUpdatedDate(LocalDateTime.now());
        // userReq.setUpdatedBy("Dat Vu");
        transactionReq.setIsDeleted(Boolean.FALSE);

        try {
            // Save to database
            TransactionEntity transactionEntity = modelMapper.map(transactionReq, TransactionEntity.class);
            transactionEntity = transactionRepository.save(transactionEntity);

            // return DtoResponse
            TransactionDto transactionResp = modelMapper.map(transactionEntity, TransactionDto.class);
            return transactionResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<TransactionDto> getTransactions(int page, int limit, HttpServletRequest req) {

        page = page > 0 ? page - 1 : 0;

        try {
            Pageable pageableReq = PageRequest.of(page, limit);
            Page<TransactionEntity> transactionPage = transactionRepository.findAll(pageableReq);
            List<TransactionEntity> transactionRecords = transactionPage.getContent();

            if (transactionRecords.isEmpty()) {
                throw new AppException(ErrorMessage.NO_RECORD_FOUND.getMessage(),
                        HttpStatus.NOT_FOUND);
            }

            // Convert and return DtoResp List
            List<TransactionDto> transactionsResp = new ArrayList<>();
            for (TransactionEntity transaction : transactionRecords) {
                TransactionDto transactionResp = modelMapper.map(transaction, TransactionDto.class);
                transactionsResp.add(transactionResp);
            }
            return transactionsResp;
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
