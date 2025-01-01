package com.controlleradvicelogging.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private String error;

    private String traceId;
}
