package com.zup.bootcamp.model;

import com.zup.bootcamp.model.converter.CryptoConverter;
import com.zup.bootcamp.model.enums.StatusProposta;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Proposta {

    @Deprecated
    public Proposta(){
    }

    @Id
    @Type(type="uuid-char")
    private UUID id = UUID.randomUUID();

    @NotBlank
    private String nome;

    @NotBlank
    @Column(unique=true)
    @Convert(converter = CryptoConverter.class)
    private String documento;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String endereco;

    @NotNull
    @Positive
    private BigDecimal salario;

    @Enumerated
    private StatusProposta status;

    @OneToOne
    private Cartao cartao;

    @PastOrPresent
    private LocalDateTime instanteInclusao = LocalDateTime.now();

    public Proposta(@NotBlank String nome, @NotBlank String documento,
                    @NotBlank @Email String email, @NotBlank String endereco,
                    @NotNull @Positive BigDecimal salario) {
        this.nome = nome;
        this.documento = documento;
        this.email = email;
        this.endereco = endereco;
        this.salario = salario;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
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

    public String getDocumento() {
        return documento;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public LocalDateTime getInstanteInclusao() {
        return instanteInclusao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public void atualizaStatus(StatusProposta statusProposta) {
        this.status = statusProposta;
    }
}
