package com.zup.bootcamp.client.request;

import javax.validation.constraints.NotNull;

public class BloqueioRequest {

    @NotNull
    private String sistemaResponsavel;

    public BloqueioRequest(@NotNull String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }
}
