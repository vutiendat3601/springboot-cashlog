package com.dv.cashlog.api.request;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    @NotNull(message = "Email must not be null")
    @Email
    private String email;

    @NotNull(message = "Password must not be null")
    @NotBlank(message = "Password must not be blank")
    private String password;

    private String phone;

    @NotNull(message = "Name must not be null")
    @NotBlank(message = "Name must not be blank")
    private String fullName;

    private LocalDate birthday;

    private String userCode;

    @NotNull(message = "Name of role must not be null")
    @NotBlank(message = "Name of role must not be blank")
    private String nameOfRole;

    private String nameOfClass;
}
