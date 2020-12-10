package com.zup.bootcamp.client.response;

import com.zup.bootcamp.model.enums.ResultadoCarteira;

public class CarteiraResponse {

    private String id;
    private ResultadoCarteira resultado;

    @Deprecated
    public CarteiraResponse() {
    }

    public String getId() {
        return id;
    }

    public ResultadoCarteira getResultado() {
        return resultado;
    }
}
