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
import com.zup.bootcamp.model.enums.StatusProposta;
import com.zup.bootcamp.validation.DocumentoCpfCnpjValidator;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
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

    private final Logger logger = LoggerFactory.getLogger(PropostaController.class);

    private final Tracer tracer;

    public PropostaController(Tracer tracer) {
        this.tracer = tracer;
    }

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

    @PostMapping
    public ResponseEntity<?> criaProposta(@RequestBody @Valid PropostaRequest propostaRequest,
                                          UriComponentsBuilder builder) throws JsonProcessingException {

        Span activeSpan = tracer.activeSpan();
        activeSpan.setTag("user.email", "girlaine.azevedo@zup.com.br");
        activeSpan.setBaggageItem("user.email", "girlaine.azevedo@zup.com.br");

        if(propostaRequest.existeDocumento(propostaRepository))
            return ResponseEntity.unprocessableEntity().body("Já existe uma proposta para este solicitante" );

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

        logger.info("Proposta criada com sucesso!");

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
