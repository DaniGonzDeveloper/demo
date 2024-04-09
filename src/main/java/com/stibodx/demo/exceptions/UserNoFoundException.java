package com.stibodx.demo.exceptions;

public class UserNoFoundException extends RuntimeException{
    public UserNoFoundException(String msg) {
        super(msg);
    }

}
