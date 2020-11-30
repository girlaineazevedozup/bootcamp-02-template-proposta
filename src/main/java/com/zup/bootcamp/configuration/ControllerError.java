package com.zup.bootcamp.configuration;

import java.util.ArrayList;
import java.util.List;

public class ControllerError {

    private List<String> globalErrorsMessages = new ArrayList<>();
    private List<FieldErrorController> fieldErrors = new ArrayList<>();

    public void addError(String message){
        globalErrorsMessages.add(message);
    }

    public void addFieldError(String field, String message){
        FieldErrorController fieldError = new FieldErrorController(field, message);
        fieldErrors.add(fieldError);
    }

    public List<String> getGlobalErrorsMessages() {
        return globalErrorsMessages;
    }

    public List<FieldErrorController> getErrors() {
        return fieldErrors;
    }



}
