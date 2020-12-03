package com.zup.bootcamp.configuration.exception;

public class FieldErrorController {

    private String field;
    private String message;

    FieldErrorController(){}

    public FieldErrorController(String field, String message){
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
