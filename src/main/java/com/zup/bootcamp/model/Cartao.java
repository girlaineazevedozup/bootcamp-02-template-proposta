package com.zup.bootcamp.model;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
public class Cartao {

    @Id
    @Type(type="uuid-char")
    private UUID id = UUID.randomUUID();

    @NotBlank
    private String numero;

    @Deprecated
    public Cartao(){
    }

    public Cartao(@NotBlank String numero) {
        this.numero = numero;
    }

    public String getNumero() {
        return numero;
    }

    public UUID getId() {
        return id;
    }
}
