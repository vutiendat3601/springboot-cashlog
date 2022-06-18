package com.dv.cashlog.api.request;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {

    private String phone;

    @NotNull(message = "Name must not be null")
    @NotBlank(message = "Name must not be blank")
    private String fullName;

    private LocalDate birthday;
}
