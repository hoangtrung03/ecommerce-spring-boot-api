package com.example.ecommerce.dto.response;

import org.springframework.http.HttpStatus;

public class ResultResponse {

    private Integer code; // Status code. e.g., 200

    private String message; // Response message

    private Object data; // The response payload


    public ResultResponse(HttpStatus unauthorized, String authenticationFailed) {
    }

    public ResultResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultResponse(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}