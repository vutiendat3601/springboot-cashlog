package com.dv.cashlog.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassResponse {
    private long id;

    private String name;

    private String description;

    private MajorResponse major;

}
