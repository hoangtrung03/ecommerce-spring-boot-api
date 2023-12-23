package com.example.ecommerce.dto.response;

import org.springframework.http.HttpStatus;

public class ResultResponse<T> {

    private Integer code; // Status code. e.g., 200

    private String message; // Response message

    private T data; // The response payload


    public ResultResponse(HttpStatus unauthorized, String authenticationFailed) {
    }

    public ResultResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultResponse(Integer code, String message, T data) {
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}