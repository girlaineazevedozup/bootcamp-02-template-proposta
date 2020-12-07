package com.zup.bootcamp.client.request;

import java.util.UUID;

public class SolicitacaoAnaliseRequest {

    private String documento;
    private String nome;
    private UUID   idProposta;

    public SolicitacaoAnaliseRequest(String documento, String nome, UUID idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public UUID getIdProposta() {
        return idProposta;
    }
}
