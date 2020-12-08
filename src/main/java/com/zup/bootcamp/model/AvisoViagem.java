package com.zup.bootcamp.model;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class AvisoViagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @PastOrPresent
    private LocalDateTime instanteInclusao = LocalDateTime.now();

    @NotBlank
    private String enderecoRemoto;

    @NotBlank
    private String userAgent;

    @NotBlank
    private String destino;

    @NotNull
    @Future
    private LocalDate dataFimViagem;

    @NotNull
    @OneToOne
    private Cartao cartao;

    @Deprecated
    public AvisoViagem(){
    }
    public AvisoViagem(@NotNull Cartao cartao, @NotBlank String enderecoRemoto,
                       @NotBlank String userAgent, @NotBlank String destino,
                       @NotNull @Future LocalDate dataFimViagem) {
        this.enderecoRemoto = enderecoRemoto;
        this.userAgent = userAgent;
        this.destino = destino;
        this.dataFimViagem = dataFimViagem;
        this.cartao = cartao;
    }
}
