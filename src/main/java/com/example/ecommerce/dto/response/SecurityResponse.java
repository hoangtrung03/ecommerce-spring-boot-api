package com.example.ecommerce.dto.response;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityResponse {
    public void sendLogoutResponse(HttpServletResponse response, String message, int code) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = generateLogoutResponse(message, code);
        response.getWriter().write(jsonResponse);
        response.setStatus(code);
    }

    private String generateLogoutResponse(String message, Integer code) {
        ResultResponse<String> resultResponse = new ResultResponse<>(HttpStatus.OK.value(), message);

        return "{"
                + "\"code\":" + code + ","
                + "\"message\":\"" + message + "\","
                + "\"data\":\"" + resultResponse.getData() + "\""
                + "}";
    }
}
