package com.dv.cashlog.exception;

public enum ErrorMessage {
    NAME_WAS_EXISTED("Name was existed!!!"),
    NO_RECORD_FOUND("No record found!!!"),
    EMAIL_WAS_EXISTED("Email was exisited!!!"),
    FILE_NOT_FOUND("File was not found!!!");

    private String message;

    public String getMessage() {
        return message;
    }

    private ErrorMessage(String message) {
        this.message = message;
    }
}
