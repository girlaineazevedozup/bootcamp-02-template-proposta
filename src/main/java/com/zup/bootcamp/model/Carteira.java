package com.zup.bootcamp.model;

import com.zup.bootcamp.model.enums.TipoCarteira;
import com.zup.bootcamp.model.converter.CryptoConverter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @NotBlank
    private String email;

    @NotNull
    @Enumerated
    private TipoCarteira carteira;

    @NotNull
    @OneToOne
    private Cartao cartao;

    private String numeroCarteira;

    @Deprecated
    public Carteira(){
    }

    public Carteira(@NotBlank String email, @NotNull TipoCarteira carteira,
                    @NotNull Cartao cartao) {
        this.email = email;
        this.carteira = carteira;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }

    public void setNumeroCarteira(String numeroCarteira) {
        this.numeroCarteira = numeroCarteira;
    }
}
