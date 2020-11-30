package com.zup.bootcamp.controller;

import com.zup.bootcamp.model.Proposta;
import com.zup.bootcamp.request.PropostaRequest;
import com.zup.bootcamp.validation.DocumentoCpfCnpjValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.net.URI;

@RestController
public class PropostaController {

    @PersistenceContext
    private EntityManager manager;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(new DocumentoCpfCnpjValidator());
    }

    @PostMapping(value = "/propostas")
    @Transactional
    public ResponseEntity<?> criaProposta(@RequestBody @Valid PropostaRequest propostaRequest,
                                          UriComponentsBuilder builder) {
        Proposta novaProposta = propostaRequest.toModel();
        manager.persist(novaProposta);

        URI enderecoConsulta = builder.path("/propostas/{id}").build(novaProposta.getId());
        return ResponseEntity.created(enderecoConsulta).build();
    }

}
