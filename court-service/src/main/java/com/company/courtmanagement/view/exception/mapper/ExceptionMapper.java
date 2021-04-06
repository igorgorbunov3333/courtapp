package com.company.courtmanagement.view.exception.mapper;

import com.company.courtmanagement.service.exception.ConflictException;
import com.company.courtmanagement.service.exception.ResourceNotFoundException;
import com.company.courtmanagement.service.exception.ResourceNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionMapper {

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handleException(Throwable throwable) {
        if (throwable instanceof ResourceNotValidException) {
            return new ResponseEntity<>(throwable.getMessage(), HttpStatus.BAD_REQUEST);
        }
        if (throwable instanceof MethodArgumentNotValidException) {
            List<FieldError> fieldErrors =
                    ((MethodArgumentNotValidException) throwable).getBindingResult().getFieldErrors();
            String errMessage = "Fields '" + fieldErrors.stream()
                    .map(fieldError -> fieldError.getField()
                            + "="
                            + fieldError.getRejectedValue()).collect(Collectors.joining(", "))
                            + "' are not valid";
            return new ResponseEntity<>(errMessage, HttpStatus.BAD_REQUEST);
        }
        if (throwable instanceof ConflictException) {
            return new ResponseEntity<>(throwable.getMessage(), HttpStatus.CONFLICT);
        }
        if (throwable instanceof ResourceNotFoundException) {
            return new ResponseEntity<>(throwable.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(throwable.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
