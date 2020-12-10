package com.zup.bootcamp.model;

import com.zup.bootcamp.model.converter.CryptoConverter;
import com.zup.bootcamp.model.enums.StatusCartao;
import org.hibernate.annotations.Type;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
public class Cartao {

    @Id
    @Type(type="uuid-char")
    private UUID id = UUID.randomUUID();

    @NotBlank
    @Convert(converter = CryptoConverter.class)
    private String numero;

    @Enumerated
    private StatusCartao status;

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

    public void bloqueia() {
        this.status = StatusCartao.BLOQUEADO;
    }
}
