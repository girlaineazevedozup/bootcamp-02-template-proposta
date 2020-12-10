package com.zup.bootcamp.controller;

import com.zup.bootcamp.client.CartaoClient;
import com.zup.bootcamp.client.request.AvisoRequest;
import com.zup.bootcamp.client.request.BloqueioRequest;
import com.zup.bootcamp.client.request.CarteiraClientRequest;
import com.zup.bootcamp.client.response.CarteiraResponse;
import com.zup.bootcamp.client.response.ResultadoResponse;
import com.zup.bootcamp.controller.request.AvisoViagemRequest;
import com.zup.bootcamp.controller.request.CarteiraRequest;
import com.zup.bootcamp.infrastructure.BloqueioRepository;
import com.zup.bootcamp.infrastructure.CartaoRepository;
import com.zup.bootcamp.infrastructure.CarteiraRepository;
import com.zup.bootcamp.model.AvisoViagem;
import com.zup.bootcamp.model.Bloqueio;
import com.zup.bootcamp.model.Cartao;
import com.zup.bootcamp.model.Carteira;
import com.zup.bootcamp.model.enums.ResultadoCarteira;
import com.zup.bootcamp.model.enums.StatusAvisoViagem;
import com.zup.bootcamp.model.enums.StatusCartao;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private BloqueioRepository bloqueioRepository;

    @Autowired
    private CarteiraRepository carteiraRepository;

    @Autowired
    private CartaoClient cartaoClient;

    @PersistenceContext
    private EntityManager manager;

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
            ResultadoResponse resultadoResponse = cartaoClient.bloqueiaCartao(
                    cartao.getNumero(), new BloqueioRequest(userAgent));

            if(StatusCartao.BLOQUEADO.name().equals(resultadoResponse.getResultado())){
                cartao.bloqueia();
                manager.persist(cartao);
            }
        }catch (FeignException ex){
            throw new ResponseStatusException(HttpStatus.valueOf(ex.status()), ex.contentUTF8());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{idCartao}/avisos")
    @Transactional
    public ResponseEntity<?> avisaViagem(@PathVariable(name = "idCartao") @Valid UUID idCartao,
                                         HttpServletRequest request,
                                         @RequestHeader(value = "User-Agent") String userAgent,
                                         @RequestBody @Valid AvisoViagemRequest avisoViagemRequest) {

        Optional<Cartao> cartaoOptional = cartaoRepository.findById(idCartao);

        Cartao cartao = cartaoOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado!"));

        try{
            ResultadoResponse resultadoResponse = cartaoClient.avisaViagem(
                    cartao.getNumero(), new AvisoRequest(avisoViagemRequest.getDestino(),
                            avisoViagemRequest.getDataFimViagem()));

            if(StatusAvisoViagem.CRIADO.name().equals(resultadoResponse.getResultado())){
                AvisoViagem avisoViagem = avisoViagemRequest.toModel(cartao, request.getRemoteAddr(), userAgent);
                manager.persist(avisoViagem);
            }
        }catch (FeignException ex){
            throw new ResponseStatusException(HttpStatus.valueOf(ex.status()), ex.contentUTF8());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{idCartao}/carteiras")
    @Transactional
    public ResponseEntity<?> associaCarteira(@PathVariable(name = "idCartao") @Valid UUID idCartao,
                                             @RequestBody @Valid CarteiraRequest carteiraRequest,
                                             UriComponentsBuilder builder) {

        Cartao cartao = cartaoRepository.findById(idCartao).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado!"));

        if(carteiraRepository.findByCartaoAndCarteira(cartao, carteiraRequest.getCarteira()).isPresent())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                        "O cartão já está associado a carteira informada.");

        Carteira carteira = carteiraRequest.toModel(cartao);
        try{
            CarteiraResponse carteiraResponse = cartaoClient.associaCartao(
                    cartao.getNumero(), new CarteiraClientRequest(carteiraRequest.getEmail(),
                            carteiraRequest.getCarteira().name()));

            if(ResultadoCarteira.ASSOCIADA.equals(carteiraResponse.getResultado())){
                carteira.setNumeroCarteira(carteiraResponse.getId());
                manager.persist(carteira);
            }
        }catch (FeignException ex){
            throw new ResponseStatusException(HttpStatus.valueOf(ex.status()), ex.contentUTF8());
        }

        URI enderecoCarteira = builder.path("/{idCartao}/carteiras/{id}")
                .build(idCartao, carteira);

        return ResponseEntity.created(enderecoCarteira).build();
    }
}
