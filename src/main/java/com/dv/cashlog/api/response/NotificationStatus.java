package com.dv.cashlog.api.response;

public enum NotificationStatus {
    
    SUCCESSFUL("Successful!!!");

    private String message;

    public String getMessage() {
        return message;
    }

    private NotificationStatus(String message) {
        this.message = message;
    }
}
