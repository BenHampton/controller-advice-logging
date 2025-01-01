package com.controlleradvicelogging.exception;

import com.controlleradvicelogging.model.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.controlleradvicelogging.exception.GlobalExceptionHandler.TRACE_ID_HEADER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private final static String TRACE_ID = "66d0d121c45d61c642253e76b90ea442";
    
    private final static String TEST_ERROR = "TEST_ERROR";

    @InjectMocks
    GlobalExceptionHandler underTest;

    @BeforeEach
    public void setup() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(TRACE_ID_HEADER, TRACE_ID);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void emptyTraceId() {
        RequestContextHolder.setRequestAttributes(null);
        NotFoundException exception = new NotFoundException(TEST_ERROR);
        ResponseEntity<ErrorResponse> response = underTest.handleNotFoundException(exception);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(TEST_ERROR, response.getBody().getError());
        assertTrue(response.getBody().getTraceId().isEmpty());
    }

    @Test
    void nullTraceId() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
        NotFoundException exception = new NotFoundException(TEST_ERROR);
        ResponseEntity<ErrorResponse> response = underTest.handleNotFoundException(exception);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(TEST_ERROR, response.getBody().getError());
        assertNull(response.getBody().getTraceId());
    }

    @Test
    void handleNotFoundException() {
        NotFoundException exception = new NotFoundException(TEST_ERROR);
        ResponseEntity<ErrorResponse> response = underTest.handleNotFoundException(exception);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(TEST_ERROR, response.getBody().getError());
        assertEquals(TRACE_ID, response.getBody().getTraceId());
    }

    @Test
    void handleConflictException() {
        Exception exception = new ConflictException(TEST_ERROR);
        ResponseEntity<ErrorResponse> response = underTest.handleConflictException(exception);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(TEST_ERROR, response.getBody().getError());
        assertEquals(TRACE_ID, response.getBody().getTraceId());
    }

    @Test
    void handleInternalServerException() {
        Exception exception = new InternalServerException(TEST_ERROR);
        ResponseEntity<ErrorResponse> response = underTest.handleInternalServerException(exception);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(TEST_ERROR, response.getBody().getError());
        assertEquals(TRACE_ID, response.getBody().getTraceId());
    }

    @Test
    void handleBadRequestException() {
        Exception exception = new BadRequestException(TEST_ERROR);
        ResponseEntity<ErrorResponse> response = underTest.handleBadRequestException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(TEST_ERROR, response.getBody().getError());
        assertEquals(TRACE_ID, response.getBody().getTraceId());
    }

    @Test
    void handleForbiddenException() {
        Exception exception = new ForbiddenException(TEST_ERROR);
        ResponseEntity<ErrorResponse> response = underTest.handleForbiddenException(exception);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(TEST_ERROR, response.getBody().getError());
        assertEquals(TRACE_ID, response.getBody().getTraceId());
    }
}