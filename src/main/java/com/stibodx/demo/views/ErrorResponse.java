package com.stibodx.demo.views;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    String message;
    int statusCode;
}
