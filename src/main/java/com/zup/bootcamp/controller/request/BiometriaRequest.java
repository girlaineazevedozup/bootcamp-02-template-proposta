package com.zup.bootcamp.controller.request;

import com.zup.bootcamp.model.Biometria;
import com.zup.bootcamp.model.Cartao;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;

public class BiometriaRequest {

    @NotBlank
    private String biometria;

    public void setBiometria(String biometria) {
        this.biometria = biometria;
    }

    public boolean biometriaValida() {
        Assert.hasLength(biometria, "Biometria vazia.");
        return Base64.isBase64(biometria);
    }

    public Biometria toModel(Cartao cartao){
        return new Biometria(Base64.decodeBase64(biometria), cartao);
    }
}
