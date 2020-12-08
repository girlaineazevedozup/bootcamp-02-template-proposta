package com.zup.bootcamp.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zup.bootcamp.model.AvisoViagem;
import com.zup.bootcamp.model.Cartao;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class AvisoViagemRequest {

    @NotBlank
    private String destino;

    @Future
    @NotNull
    @JsonProperty("data_fim_viagem")
    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate dataFimViagem;

    public AvisoViagemRequest(@NotBlank String destino,
                              @Future @NotNull LocalDate dataFimViagem) {
        this.destino = destino;
        this.dataFimViagem = dataFimViagem;
    }

    public AvisoViagem toModel(Cartao cartao, String remoteAddr, String userAgent) {
        return new AvisoViagem(cartao, remoteAddr, userAgent, destino, dataFimViagem);
    }

    public String getDestino() {
        return destino;
    }

    public LocalDate getDataFimViagem() {
        return dataFimViagem;
    }
}
