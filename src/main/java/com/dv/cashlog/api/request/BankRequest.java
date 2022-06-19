package com.dv.cashlog.api.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankRequest {
    
    @NotNull(message = "Class name must be not null!!!")
    @NotBlank(message = "Class name must be not blank!!!")
    private String name;

    private String description;
}
