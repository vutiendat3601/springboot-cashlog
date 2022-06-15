package com.dv.cashlog.common.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDto {

    private Long id;
    private LocalDateTime dateTime;
    private String particular;
    private Double collected;
    private Double paid;
    private String src_money;
}
