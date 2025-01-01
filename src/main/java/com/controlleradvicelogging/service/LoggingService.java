package com.controlleradvicelogging.service;

import com.controlleradvicelogging.model.FooBar;
import com.controlleradvicelogging.exception.BadRequestException;
import com.controlleradvicelogging.exception.ConflictException;
import com.controlleradvicelogging.exception.ForbiddenException;
import com.controlleradvicelogging.exception.InternalServerException;
import com.controlleradvicelogging.exception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {

    public String getFooBar() {
        return "FOO BAR";
    }

    public String testPath(String foo, String bar) {
        return "testPathVariable: " + foo + " testRequestParam: " + bar;
    }

    public FooBar testBody(FooBar fooBar) {
        return fooBar;
    }

    public String testNotFoundException() {
        throw new NotFoundException("test NotFoundException");
    }

    public String testBadRequestException() {
        throw new BadRequestException("test BadRequest");
    }

    public String testForbiddenException() {
        throw new ForbiddenException("test Forbidden");
    }

    public String testInternalServerException() {
        throw new InternalServerException("test InternalServer");
    }

    public String testConflictException() {
        throw new ConflictException("test Conflict");
    }
}
