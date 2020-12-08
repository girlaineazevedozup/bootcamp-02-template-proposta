package com.zup.bootcamp.client.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class BloqueioRequest {

    @NotBlank
    private String sistemaResponsavel;

    public BloqueioRequest(@NotNull String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }
}
