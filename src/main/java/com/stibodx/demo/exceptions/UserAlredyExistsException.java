package com.stibodx.demo.exceptions;

public class UserAlredyExistsException extends RuntimeException{
    public UserAlredyExistsException(String msg) {
        super(msg);
    }
}
