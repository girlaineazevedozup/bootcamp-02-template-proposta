package com.zup.bootcamp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Entity
public class Bloqueio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @PastOrPresent
    private LocalDateTime instanteInclusao = LocalDateTime.now();

    @NotBlank
    private String userAgent;

    @NotBlank
    private String enderecoRemoto;

    @NotNull
    @OneToOne
    private Cartao cartao;

    @Deprecated
    public Bloqueio(){
    }

    public Bloqueio(@NotBlank String userAgent, @NotBlank String enderecoRemoto,
                    @NotNull Cartao cartao) {
        this.userAgent = userAgent;
        this.enderecoRemoto = enderecoRemoto;
        this.cartao = cartao;
    }
}
