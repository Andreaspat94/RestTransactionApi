package com.apaterakis.resttransactionapi.exception;

import com.apaterakis.resttransactionapi.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exc) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exc.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException exc, HandlerMethod handlerMethod) {
        String invalidValue = exc.getValue().toString();
        String message = "";
        String controllerName = handlerMethod.getBeanType().getSimpleName();
        if (controllerName.equals("BeneficiaryController")) {
            message = "The parameter type `" + invalidValue + "` is invalid. `Long` type is required ";
        }
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
