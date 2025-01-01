package com.controlleradvicelogging.exception;

import com.controlleradvicelogging.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final String TRACE_ID_HEADER = "x-b3-traceid";

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(buildErrorResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(buildErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(buildErrorResponse(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(buildErrorResponse(exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = InternalServerException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(buildErrorResponse(exception.getMessage()),  HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(buildErrorResponse(exception.getMessage()), HttpStatus.FORBIDDEN);
    }

    private ErrorResponse buildErrorResponse(String errorMessage) {
        return ErrorResponse.builder().error(errorMessage)
                .traceId(getTraceId())
                .build();
    }

    private String getTraceId() {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            return "";
        }
        return requestAttributes.getRequest().getHeader(TRACE_ID_HEADER);
    }
}
