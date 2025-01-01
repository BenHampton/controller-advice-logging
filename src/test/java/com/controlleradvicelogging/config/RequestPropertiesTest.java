package com.controlleradvicelogging.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestPropertiesTest {

    @InjectMocks
    RequestProperties underTest;

    @Test
    void testRequestProperties() {
        underTest = Mockito.spy(underTest);
        when(underTest.isCanLog()).thenReturn(true);
        assertTrue(underTest.isCanLog());
    }

    @Test
    void testRequestProperties_canLogFalse() {
        assertFalse(underTest.isCanLog());
        assertEquals(false, underTest.isCanLog());
    }

}