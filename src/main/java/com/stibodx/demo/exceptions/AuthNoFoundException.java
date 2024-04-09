package com.stibodx.demo.exceptions;

public class AuthNoFoundException extends RuntimeException{
    public AuthNoFoundException(String msg) {
        super(msg);
    }

}
