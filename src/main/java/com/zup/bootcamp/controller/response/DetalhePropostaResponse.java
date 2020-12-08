package com.zup.bootcamp.controller.response;

import com.zup.bootcamp.model.Proposta;
import com.zup.bootcamp.model.enums.StatusProposta;

import java.math.BigDecimal;
import java.util.UUID;

public class DetalhePropostaResponse {

    private String nome;
    private String documento;
    private String email;
    private String endereco;
    private BigDecimal salario;
    private StatusProposta status;
    private UUID idCartao;

    public DetalhePropostaResponse(Proposta proposta) {
        this.nome = proposta.getNome();
        this.documento = proposta.getDocumento();
        this.email = proposta.getEmail();
        this.endereco = proposta.getEndereco();
        this.salario = proposta.getSalario();
        this.status = proposta.getStatus();
        if(proposta.getCartao() != null)
            idCartao = proposta.getCartao().getId();
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }

    public String getEmail() {
        return email;
    }

    public String getEndereco() {
        return endereco;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public StatusProposta getStatus() {
        return status;
    }

    public UUID getIdCartao() {
        return idCartao;
    }
}
