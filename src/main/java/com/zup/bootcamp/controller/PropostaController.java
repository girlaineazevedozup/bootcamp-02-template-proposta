package com.zup.bootcamp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.bootcamp.client.AnaliseClient;
import com.zup.bootcamp.client.request.SolicitacaoAnaliseRequest;
import com.zup.bootcamp.client.response.ResultadoAnaliseResponse;
import com.zup.bootcamp.controller.request.PropostaRequest;
import com.zup.bootcamp.controller.response.DetalhePropostaResponse;
import com.zup.bootcamp.infrastructure.ExecutorTransacao;
import com.zup.bootcamp.infrastructure.PropostaRepository;
import com.zup.bootcamp.model.Proposta;
import com.zup.bootcamp.model.StatusProposta;
import com.zup.bootcamp.validation.DocumentoCpfCnpjValidator;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private ExecutorTransacao executorTransacao;

    @Autowired
    private AnaliseClient analiseClient;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(new DocumentoCpfCnpjValidator());
    }

    private final Logger logger = LoggerFactory.getLogger(PropostaController.class);

    @PostMapping
    public ResponseEntity<?> criaProposta(@RequestBody @Valid PropostaRequest propostaRequest,
                                          UriComponentsBuilder builder) throws JsonProcessingException {

        if(propostaRequest.existeDocumento(propostaRepository))
            return ResponseEntity.unprocessableEntity().body("Já existe uma proposta para o solicitante: " + propostaRequest.getDocumento()  );

        Proposta proposta = propostaRequest.toModel();
        executorTransacao.salvaEComita(proposta);

        try{
            ResultadoAnaliseResponse resultadoAnalise = analiseClient.solicitacaoAnalise(
                    new SolicitacaoAnaliseRequest(proposta.getDocumento(), proposta.getDocumento(),
                            proposta.getId()));

            if(resultadoAnalise.semRestricao())
                proposta.atualizaStatus(StatusProposta.ELEGIVEL);

        }catch (FeignException.UnprocessableEntity ex){
            ResultadoAnaliseResponse resultadoAnalise = new ObjectMapper().readValue(ex.contentUTF8(), ResultadoAnaliseResponse.class);
            if(resultadoAnalise.temRestricao())
                proposta.atualizaStatus(StatusProposta.NAO_ELEGIVEL);
        }

        executorTransacao.atualizaEComita(proposta);

        logger.info("Proposta do solicitante documento={} criada com sucesso!", proposta.getDocumento());

        URI enderecoConsulta = builder.path("/propostas/{id}").build(proposta.getId());
        return ResponseEntity.created(enderecoConsulta).build();
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<?> detalhaProposta(@PathVariable("id") UUID id){
        Optional<Proposta> proposta = propostaRepository.findById(id);

        if(proposta.isEmpty())
            return ResponseEntity.notFound().build();
        DetalhePropostaResponse detalhePropostaResponse = new DetalhePropostaResponse(proposta.get());

        return ResponseEntity.ok(detalhePropostaResponse);
    }
}
