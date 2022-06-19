package com.dv.cashlog.api.request;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionRequest {
    private String particular;

    @Min(value = 0)
    private double collected;

    @Min(value = 0)
    private double paid;

    private String userCodeOfUser;

    private String nameOfBank;
}
