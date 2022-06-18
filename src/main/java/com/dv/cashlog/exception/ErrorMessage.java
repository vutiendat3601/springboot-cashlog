package com.dv.cashlog.exception;

public enum ErrorMessage {
    NAME_WAS_EXISTED("Name was existed!!!"),
    NO_RECORD_FOUND("No record found!!!"),
    EMAIL_WAS_EXISTED("Email was taken!!!"),
    FILE_NOT_FOUND("File was not found!!!"),
    ROLE_NOT_FOUND("Role was not found!!!"),
    USER_NOT_FOUND("User was not found!!!"),
    CLASS_WAS_EXISTED("Class was existed!!!"),
    MAJOR_NOT_FOUND("Major was not found!!!"),
    CLASS_NOT_FOUND("Class was not found!!!");

    private String message;

    public String getMessage() {
        return message;
    }

    private ErrorMessage(String message) {
        this.message = message;
    }
}
