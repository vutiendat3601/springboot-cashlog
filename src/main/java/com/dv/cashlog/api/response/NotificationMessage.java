package com.dv.cashlog.api.response;

public enum NotificationMessage {
    SUCCESSFUL("successful");

    private String message;

    public String getMessage() {
        return message;
    }

    private NotificationMessage(String message) {
        this.message = message;
    }
}
