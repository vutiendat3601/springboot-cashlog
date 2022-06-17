package com.dv.cashlog.api.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private String phone;
    private String fullName;
    private LocalDate birthday;
}
