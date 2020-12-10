package com.zup.bootcamp.client.request;

public class CarteiraClientRequest {

    private String email;
    private String carteira;

    public CarteiraClientRequest(String email, String carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public String getEmail() {
        return email;
    }

    public String getCarteira() {
        return carteira;
    }
}
