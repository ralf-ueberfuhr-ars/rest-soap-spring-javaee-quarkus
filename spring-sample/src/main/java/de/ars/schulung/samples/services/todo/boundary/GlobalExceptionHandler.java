package de.ars.schulung.samples.services.todo.boundary;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Übersetzt in der Boundary geworfene Exceptions in HTTP Responses. Damit elimiert man einen Großteil der ResponseEntity-Erzeugungen im Controller.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(code = BAD_REQUEST)
    protected void handleValidationException() {
    }

}
