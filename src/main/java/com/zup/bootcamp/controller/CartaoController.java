package com.zup.bootcamp.controller;

import com.zup.bootcamp.client.CartaoClient;
import com.zup.bootcamp.client.request.BloqueioRequest;
import com.zup.bootcamp.client.response.BloqueioResponse;
import com.zup.bootcamp.controller.request.BiometriaRequest;
import com.zup.bootcamp.infrastructure.BiometriaRepository;
import com.zup.bootcamp.infrastructure.BloqueioRepository;
import com.zup.bootcamp.infrastructure.CartaoRepository;
import com.zup.bootcamp.model.Biometria;
import com.zup.bootcamp.model.Bloqueio;
import com.zup.bootcamp.model.Cartao;
import com.zup.bootcamp.model.enums.StatusCartao;
import com.zup.bootcamp.validation.BiometriaValidator;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private BiometriaRepository biometriaRepository;

    @Autowired
    private BloqueioRepository bloqueioRepository;

    @Autowired
    private CartaoClient cartaoClient;

    @PersistenceContext
    private EntityManager manager;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(new BiometriaValidator());
    }

    @PostMapping("/{idCartao}/biometrias")
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

    @PostMapping("/{idCartao}/bloqueios")
    @Transactional
    public ResponseEntity<?> bloqueiaCartao(@PathVariable(name = "idCartao") @Valid UUID idCartao,
                                            HttpServletRequest request,
                                            @RequestHeader(value = "User-Agent") String userAgent) {

        Optional<Cartao> cartaoOptional = cartaoRepository.findById(idCartao);

        Cartao cartao = cartaoOptional.orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado!"));

        if(bloqueioRepository.findByCartao(cartao).isPresent())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cartão já está bloqueado!");

        Bloqueio bloqueio = new Bloqueio(userAgent, request.getRemoteAddr(), cartao);
        manager.persist(bloqueio);

        try{
            BloqueioResponse bloqueioResponse = cartaoClient.bloqueiaCartao(
                    cartao.getNumero(), new BloqueioRequest(userAgent));

            if(StatusCartao.BLOQUEADO.name().equals(bloqueioResponse.getResultado())){
                cartao.bloqueia();
                manager.persist(cartao);
            }
        }catch (FeignException ex){
            throw new ResponseStatusException(HttpStatus.valueOf(ex.status()), ex.contentUTF8());
        }

        return ResponseEntity.ok().build();
    }
}
