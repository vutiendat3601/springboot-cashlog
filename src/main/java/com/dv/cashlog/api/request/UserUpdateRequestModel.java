package com.dv.cashlog.api.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestModel {
    private String phone;
    private String password;
    private String fullName;
    private LocalDate birthday;
    private Long roleId;
}
