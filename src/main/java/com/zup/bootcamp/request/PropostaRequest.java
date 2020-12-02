package com.zup.bootcamp.request;

import com.zup.bootcamp.infrastructure.PropostaRepository;
import com.zup.bootcamp.model.Proposta;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CNPJValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;
import org.springframework.util.Assert;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class PropostaRequest {

    @NotBlank
    private String nome;

    @NotBlank
    private String documento;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String endereco;

    @NotNull
    @Positive
    private BigDecimal salario;

    public PropostaRequest(@NotBlank String nome, @NotBlank String documento,
                           @NotBlank @Email String email, @NotBlank String endereco,
                           @NotNull @Positive BigDecimal salario) {
        super();
        this.nome = nome;
        this.documento = documento;
        this.email = email;
        this.endereco = endereco;
        this.salario = salario;
    }

    public Proposta toModel(){
        return new Proposta(nome, documento, email, endereco, salario);
    }

    public String getDocumento() {
        return documento;
    }

    public boolean documentoValido() {
        Assert.hasLength(documento, "Documento vazio.");

        CPFValidator cpfValidator = new CPFValidator();
        cpfValidator.initialize(null);

        CNPJValidator cnpjValidator = new CNPJValidator();
        cnpjValidator.initialize(null);

        return cpfValidator.isValid(documento, null) ||
                cnpjValidator.isValid(documento, null);
    }

    public boolean existeDocumento(PropostaRepository propostaRepository){
        return propostaRepository.findByDocumento(documento).isPresent();
    }
}
