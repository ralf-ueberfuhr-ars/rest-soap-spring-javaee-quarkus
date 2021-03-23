package de.ars.schulung.samples.services.todo.boundary;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Eine Exception, die geworfen werden kann, um einen 404er zu erzeugen
 * Ausnahmsweise werden hier auch gern Unchecked Exceptions verwendet. Spring macht dies ebenso.
 */
@ResponseStatus(NOT_FOUND)
public class NotFoundException extends RuntimeException {
}
