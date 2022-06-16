package com.dv.cashlog.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.dv.cashlog.api.response.Notification;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class AppHandler {
    @ExceptionHandler(value = { AppException.class })
    public ResponseEntity<Notification> handleAppException(AppException e, WebRequest req) {
        Notification notification = new Notification(LocalDateTime.now(), e.getMessage());
        log.error(notification.getMessage(), e);
        return new ResponseEntity<>(notification, new HttpHeaders(), e.getHttpStatus());
    }

}
