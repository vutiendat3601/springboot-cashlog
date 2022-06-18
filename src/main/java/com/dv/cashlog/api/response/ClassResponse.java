package com.dv.cashlog.api.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassResponse {
    private String name;

    private String description;

    private MajorResponse major;

    private List<UserResponse> users;
}
