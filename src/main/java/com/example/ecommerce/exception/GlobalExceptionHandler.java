package com.example.ecommerce.exception;

import com.example.ecommerce.dto.response.ErrorResponseModel;
import com.example.ecommerce.model.ErrorModel;
import org.hibernate.ObjectNotFoundException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
@RestController
@Configuration
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponseModel handleException(MethodArgumentNotValidException e) {
        List<ErrorModel> errorModels = processErrors(e);
        return ErrorResponseModel
                .builder()
                .errors(errorModels.get(0))
                .type("Validator")
                .build();
    }

    private List<ErrorModel> processErrors(MethodArgumentNotValidException e) {
        List<ErrorModel> validationErrorModels = new ArrayList<ErrorModel>();

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            ErrorModel validationErrorModel = ErrorModel
                    .builder()
                    .name(fieldError.getCode())
                    .message(fieldError.getDefaultMessage())
                    .build();
            validationErrorModels.add(validationErrorModel);
        }

        return validationErrorModels.stream()
                .sorted(Comparator.comparing(ErrorModel::getName))
                .collect(Collectors.toList());
    }
}
