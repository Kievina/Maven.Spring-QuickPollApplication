package io.zipcoder.tc_spring_poll_application.controller;

import io.zipcoder.tc_spring_poll_application.dto.error.ErrorDetail;
import io.zipcoder.tc_spring_poll_application.dto.error.ValidationError;
import io.zipcoder.tc_spring_poll_application.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rnfe, HttpServletRequest request) {
        String title = "Error: Resource Not Found";
        int status = 404;
        String detail = rnfe.getMessage();
        long timeStamp = new Date().getTime();
        String developerMessage = rnfe.getStackTrace().toString();

        ErrorDetail errorDetail = new ErrorDetail(title, status, detail, timeStamp, developerMessage);
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?>
    handleValidationError(MethodArgumentNotValidException manve, HttpServletRequest request) {
        String title = "Error: Method Argument Not Valid";
        int status = 400;
        String detail = manve.getMessage();
        long timeStamp = new Date().getTime();
        String developerMessage = manve.getStackTrace().toString();
// create ErrorDetail object
        ErrorDetail errorDetail = new ErrorDetail(title, status, detail, timeStamp, developerMessage);

        errorDetail.getErrors();

        List<FieldError> fieldErrors = manve.getBindingResult().getFieldErrors();
        for (FieldError fe : fieldErrors) {

            List<ValidationError> validationErrorList = errorDetail.getErrors().get(fe.getField());
            if (validationErrorList == null) {
                validationErrorList = new ArrayList<>();
                errorDetail.getErrors().put(fe.getField(), validationErrorList);
            }
            ValidationError validationError = new ValidationError();
            validationError.setCode(fe.getCode());
            validationError.setMessage(messageSource.getMessage(fe, null));
            validationErrorList.add(validationError);
        }
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }
}
