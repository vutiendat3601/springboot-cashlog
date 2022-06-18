package com.dv.cashlog.api.response;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MajorResponse {

    private long id;

    @NotNull(message = "Major name must be not null!!!")
    @NotBlank(message = "Major name must be not blank!!!")
    private String name;

    private String description;
}
