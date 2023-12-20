package com.example.ecommerce.exception;

import com.example.ecommerce.dto.response.ErrorResponseModel;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.model.ErrorModel;
import com.example.ecommerce.model.StatusCode;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResultResponse handleObjectNotFoundException(ObjectNotFoundException ex) {
        return new ResultResponse(StatusCode.NOT_FOUND, ex.getMessage());
    }

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

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ResultResponse handleAuthenticationException(Exception ex) {
        return new ResultResponse(StatusCode.UNAUTHORIZED, "username or password is incorrect.", ex.getMessage());
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ResultResponse handleInsufficientAuthenticationException(InsufficientAuthenticationException ex) {
        return new ResultResponse(StatusCode.UNAUTHORIZED, "Login credentials are missing.", ex.getMessage());
    }

    @ExceptionHandler(AccountStatusException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ResultResponse handleAccountStatusException(AccountStatusException ex) {
        return new ResultResponse(StatusCode.UNAUTHORIZED, "User account is abnormal.", ex.getMessage());
    }

//    @ExceptionHandler(InvalidBearerTokenException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    Result handleInvalidBearerTokenException(InvalidBearerTokenException ex) {
//        return new Result(false, StatusCode.UNAUTHORIZED, "The access token provided is expired, revoked, malformed, or invalid for other reasons.", ex.getMessage());
//    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ResultResponse handleAccessDeniedException(AccessDeniedException ex) {
        return new ResultResponse(StatusCode.FORBIDDEN, "No permission.", ex.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResultResponse handleAccessDeniedException(NoHandlerFoundException ex) {
        return new ResultResponse(StatusCode.NOT_FOUND, "This API endpoint is not found.", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResultResponse handleOtherException(Exception ex) {
        return new ResultResponse(StatusCode.INTERNAL_SERVER_ERROR, "A server internal error occurs.", ex.getMessage());
    }
}
