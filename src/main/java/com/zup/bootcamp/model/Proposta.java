package com.zup.bootcamp.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
public class Proposta {

    @Deprecated
    public Proposta(){
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    @Column(unique=true)
    private String documento;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String endereco;

    @NotNull
    @Positive
    private BigDecimal salario;

    private StatusProposta status;

    public Proposta(@NotBlank String nome, @NotBlank String documento,
                    @NotBlank @Email String email, @NotBlank String endereco,
                    @NotNull @Positive BigDecimal salario) {
        this.nome = nome;
        this.documento = documento;
        this.email = email;
        this.endereco = endereco;
        this.salario = salario;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void atualizaStatusElegivel() {
        this.status = StatusProposta.ELEGIVEL;
    }

    public void atualizaStatusNaoElegivel() {
        this.status = StatusProposta.NAO_ELEGIVEL;
    }
}
