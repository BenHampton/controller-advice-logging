package com.logging.controlleradvicelogging.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FooBar {

    private String requestParam;

    private String pathVariable;
}
