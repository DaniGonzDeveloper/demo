package com.stibodx.demo.exceptions;

public class JwtException extends RuntimeException{
    public JwtException(String msg) {
        super(msg);
    }
}
