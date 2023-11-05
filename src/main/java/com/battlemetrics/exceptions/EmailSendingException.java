package com.battlemetrics.exceptions;

public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String exMessage, Throwable cause) {
        super(exMessage, cause);
    }

    public EmailSendingException(String exMessage) {
        super(exMessage);
    }
}
