package com.archu.homebudgetmanager.exception;

import javax.security.sasl.AuthenticationException;

public class UserAlreadyExistAuthenticationException extends AuthenticationException {
    public UserAlreadyExistAuthenticationException(String message){
        super(message);
    }
}
