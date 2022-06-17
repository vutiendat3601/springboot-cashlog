package com.dv.cashlog.api.response;

public enum NotificationStatus {
    SUCCESSFUL("successful!!!");

    private String message;

    public String getMessage() {
        return message;
    }

    private NotificationStatus(String message) {
        this.message = message;
    }
}
