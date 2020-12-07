package com.zup.bootcamp.controller.response;

import com.zup.bootcamp.model.Proposta;
import com.zup.bootcamp.model.StatusProposta;

import java.math.BigDecimal;

public class DetalhePropostaResponse {

    private String nome;
    private String documento;
    private String email;
    private String endereco;
    private BigDecimal salario;
    private StatusProposta status;
    private String numeroCartao;

    public DetalhePropostaResponse(Proposta proposta) {
        this.nome = proposta.getNome();
        this.documento = proposta.getDocumento();
        this.email = proposta.getEmail();
        this.endereco = proposta.getEndereco();
        this.salario = proposta.getSalario();
        this.status = proposta.getStatus();
        if(proposta.getCartao() != null)
            this.numeroCartao = proposta.getCartao().getNumero();
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

    public String getNumeroCartao() {
        return numeroCartao;
    }
}
