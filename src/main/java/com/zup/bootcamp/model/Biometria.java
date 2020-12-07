package com.zup.bootcamp.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Entity
public class Biometria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @PastOrPresent
    private LocalDate dataInclusao = LocalDate.now();

    @NotNull
    @Column(nullable = false)
    @Lob
    private byte[] biometria;

    @NotNull
    @ManyToOne
    private Cartao cartao;

    @Deprecated
    public Biometria(){
    }

    public Biometria(@NotNull byte[] biometria, @NotNull Cartao cartao) {
        this.dataInclusao = dataInclusao;
        this.biometria = biometria;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }
}
