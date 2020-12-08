package com.zup.bootcamp.client.request;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class AvisoRequest {

    @NotBlank
    private String destino;

    @NotNull
    @Future
    private LocalDate validoAte;

    public AvisoRequest(@NotNull String destino,
                        @NotNull @Future LocalDate validoAte) {

        this.destino = destino;
        this.validoAte = validoAte;
    }

    public String getDestino() {
        return destino;
    }

    public LocalDate getValidoAte() {
        return validoAte;
    }
}
