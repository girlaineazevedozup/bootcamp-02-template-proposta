package com.zup.bootcamp.validation;

import com.zup.bootcamp.controller.request.PropostaRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DocumentoCpfCnpjValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return PropostaRequest.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if(errors.hasErrors())
            return;

        PropostaRequest request = (PropostaRequest)target;

        if(!request.documentoValido()){
            errors.rejectValue("documento", null, "Documento precisa ser CPF ou CNPJ");
        }
    }
}
