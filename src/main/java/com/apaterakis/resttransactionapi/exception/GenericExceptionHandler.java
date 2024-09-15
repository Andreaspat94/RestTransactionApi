package com.apaterakis.resttransactionapi.exception;

import com.apaterakis.resttransactionapi.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleDuplicateBeneficiaryException(DuplicateBeneficiaryException exc) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                exc.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException exc, HandlerMethod handlerMethod) {
        String invalidValue = exc.getValue().toString();
        String message = "";
        String controllerName = handlerMethod.getBeanType().getSimpleName();
        if (controllerName.equals("BeneficiaryController")
                || controllerName.equals("TransactionController")
                || controllerName.equals("AccountController")) {
            message = "The parameter type `" + invalidValue + "` is invalid. `Long` type is required ";
        }
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadableException(HttpMessageNotReadableException exc, HandlerMethod handlerMethod) {

        String controllerName = handlerMethod.getBeanType().getSimpleName();
        String message = "";
        if (controllerName.equals("TransactionController")) {
            message = "Invalid input. `accountId` and `amount` should be positive numbers.";
        } else if (controllerName.equals("AccountController")) {
            message = "Request parameters must be positive numbers";
        } else {
            message = "Invalid input. `firstDeposit` must be a positive number";
        }
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleInsufficientBalanceException(InsufficientBalanceException exc) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                exc.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exc) {
        String message = "Amount must be greater than zero.";
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
