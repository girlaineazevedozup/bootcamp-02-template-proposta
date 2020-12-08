package com.zup.bootcamp.controller;

import com.zup.bootcamp.controller.request.BiometriaRequest;
import com.zup.bootcamp.infrastructure.BiometriaRepository;
import com.zup.bootcamp.infrastructure.CartaoRepository;
import com.zup.bootcamp.model.Biometria;
import com.zup.bootcamp.model.Cartao;
import com.zup.bootcamp.validation.BiometriaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
public class BiometriaController {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private BiometriaRepository biometriaRepository;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(new BiometriaValidator());
    }

    @PostMapping("/cartoes/{idCartao}/biometrias")
    @Transactional
    public ResponseEntity<?> criaBiometria(@PathVariable(name = "idCartao") @Valid UUID idCartao,
                                           @RequestBody @Valid BiometriaRequest biometriaRequest,
                                           UriComponentsBuilder builder) {

        Optional<Cartao> cartaoOptional = cartaoRepository.findById(idCartao);

        if(cartaoOptional.isEmpty())
            return ResponseEntity.notFound().build();

        Biometria biometria = biometriaRequest.toModel(cartaoOptional.get());
        biometriaRepository.save(biometria);

        return ResponseEntity.created(builder.path("cartoes/{idCartao}/biometrias/{id}")
                .buildAndExpand(idCartao, biometria.getId()).toUri()).build();
    }
}
