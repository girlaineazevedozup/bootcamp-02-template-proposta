package com.zup.bootcamp.controller.request;

import com.zup.bootcamp.model.Cartao;
import com.zup.bootcamp.model.Carteira;
import com.zup.bootcamp.model.enums.TipoCarteira;

import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CarteiraRequest {

    @NotBlank
    @Email
    private String email;

    @NotNull
    private TipoCarteira carteira;

    public CarteiraRequest(@NotBlank String email,
                           @Future @NotNull TipoCarteira carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public Carteira toModel(Cartao cartao) {
        return new Carteira(email, carteira, cartao);
    }

    public TipoCarteira getCarteira() {
        return carteira;
    }

    public String getEmail() {
        return email;
    }
}
