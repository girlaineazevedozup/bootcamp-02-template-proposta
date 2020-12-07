package com.zup.bootcamp.validation;

import com.zup.bootcamp.controller.request.BiometriaRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class BiometriaValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return BiometriaRequest.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if(errors.hasErrors())
            return;

        BiometriaRequest request = (BiometriaRequest)target;

        if(!request.biometriaValida()){
            errors.rejectValue("biometria", null, "Biometria precisa ser em Base64");
        }
    }
}
