package com.controlleradvicelogging.logging;

import com.controlleradvicelogging.config.RequestProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HttpLoggerUtilTest {

    @InjectMocks
    HttpLoggerUtil underTest;

    @Spy
    ObjectMapper objectMapper;

    @Spy
    MockHttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    RequestProperties requestProperties;

    @ParameterizedTest
    @CsvSource({"/actuator/health, true, false", "/something/actuator/something, true, false", "/project/1, true, true",
            "/actuator/health, false, false", "/something/actuator/something, false, false", "/project/1, false, false"})
    public void shouldLogResponse(String uri, boolean canLog, boolean expected) {
        if (canLog) {
            when(request.getRequestURI()).thenReturn(uri);
        }
        when(requestProperties.isCanLog()).thenReturn(canLog);
        assertEquals(expected, underTest.shouldLogResponse(request));
    }

    @ParameterizedTest
    @CsvSource({"body, {\"id\": \"123\"}", "responseCode, 200"})
    public void buildResponseLogEntries(String key, String expected) {
        when(response.getStatus()).thenReturn(200);
        Map<String, Object> logEntries = underTest.buildResponseLogEntries(response, "{\"id\": \"123\"}");
        assertEquals(expected, logEntries.get(key).toString());
    }

    @ParameterizedTest
    @CsvSource({"method, GET", "path, /projects", "headers, {\"testKey\":\"testValue\"}"})
    public void buildRequestLogEntries(String key, String expected) {
        request = new MockHttpServletRequest("GET", "/projects");
        request.addHeader("testKey", "testValue");
        Map<String, Object> logEntries = underTest.buildRequestLogEntries(request);
        assertEquals(expected, logEntries.get(key));
    }

    @ParameterizedTest
    @CsvSource({"method, GET, \"{'id': 1}\"", "path, /projects, \"{'id': 2}\"", "headers, {\"testKey\":\"testValue\"}, \"{'id': 3}\""})
    public void includeRequestLogEntriesWhenRequestHasABody(String key, String expected, String requestBody) {
        request = new MockHttpServletRequest("GET", "/projects");
        request.addHeader("testKey", "testValue");
        Map<String, Object> logEntries = underTest.buildRequestLogEntriesWithBody(request,
                requestBody);
        assertEquals(expected, logEntries.get(key));
        assertEquals(requestBody, logEntries.get("body"));
    }

    @Test
    public void scrubAuthorizationHeaderValue() {
        request = new MockHttpServletRequest("POST", "/projects");
        request.addHeader("authorization", "Basic=mySuperSecret123456789");
        Map<String, Object> logEntries = underTest.buildRequestLogEntries(request);
        assertEquals("{\"authorization\":\"*****\"}", logEntries.get("headers"));
    }

    @Test
    public void putErrorMessageInHeadersIfConversionToJsonFails() throws JsonProcessingException {
        request = new MockHttpServletRequest("POST", "/projects");
        when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("error"){});
        Map<String, Object> logEntries = underTest.buildRequestLogEntries(request);
        assertEquals("error converting headers to json", logEntries.get("headers"));
    }

    @Test
    public void includeQueryParametersWithPath() {
        when(request.getRequestURI()).thenReturn("/projects");
        when(request.getQueryString()).thenReturn("param1=testValue");
        Map<String, Object> logEntries = underTest.buildRequestLogEntries(request);
        assertEquals("/projects?param1=testValue", logEntries.get("path"));
    }
}
