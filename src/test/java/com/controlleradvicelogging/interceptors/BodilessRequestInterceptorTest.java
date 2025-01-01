package com.controlleradvicelogging.interceptors;//package com.clayco.portal.project.interceptors;

import com.controlleradvicelogging.logging.HttpRequestLogger;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BodilessRequestInterceptorTest {

    @InjectMocks
    BodilessRequestInterceptor underTest;

    @Mock
    HttpRequestLogger httpRequestLogger;

    @Mock
    HttpServletRequest request;

    @Test
    public void logGetRequests() {
        when(request.getDispatcherType()).thenReturn(DispatcherType.REQUEST);
        when(request.getMethod()).thenReturn(HttpMethod.GET.name());
        underTest.preHandle(request, null, null);
        verify(httpRequestLogger, times(1)).log(request);
    }

    @Test
    public void logDeleteRequests() {
        when(request.getDispatcherType()).thenReturn(DispatcherType.REQUEST);
        when(request.getMethod()).thenReturn(HttpMethod.DELETE.name());
        underTest.preHandle(request, null, null);
        verify(httpRequestLogger, times(1)).log(request);
    }

    @Test
    public void doNotLogPostRequests() {
        when(request.getDispatcherType()).thenReturn(DispatcherType.REQUEST);
        when(request.getMethod()).thenReturn(HttpMethod.POST.name());
        underTest.preHandle(request, null, null);
        verifyNoInteractions(httpRequestLogger);
    }
}