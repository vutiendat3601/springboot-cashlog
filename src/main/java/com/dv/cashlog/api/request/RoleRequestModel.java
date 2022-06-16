package com.dv.cashlog.api.request;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleRequestModel {

    @NotNull(message = "Role name must be not null!!!")
    private String name;

    private String description;
}