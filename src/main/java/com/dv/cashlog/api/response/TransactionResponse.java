package com.dv.cashlog.api.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionResponse {
    private long id;

    private LocalDateTime dateTime;

    private String particular;

    private double collected;

    private double paid;

    private UserResponse user;

    private BankResponse bank;
}
