package com.zup.bootcamp.configuration.task;

import com.zup.bootcamp.client.CartaoClient;
import com.zup.bootcamp.client.response.CartaoResponse;
import com.zup.bootcamp.infrastructure.ExecutorTransacao;
import com.zup.bootcamp.infrastructure.PropostaRepository;
import com.zup.bootcamp.model.Proposta;
import com.zup.bootcamp.model.StatusProposta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartaoTask {

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private CartaoClient cartaoClient;

    @Autowired
    private ExecutorTransacao executorTransacao;

    @Scheduled(fixedDelayString = "${periodicidade.associa-cartao}")
    private void associaCartao() {

        List<Proposta> listaProposta = propostaRepository.findByStatusAndNumeroCartaoNull(StatusProposta.ELEGIVEL);

        listaProposta.forEach(proposta -> {
            CartaoResponse response = cartaoClient.solicitacaoCartao(proposta.getId());
            proposta.setNumeroCartao(response.getId());
            executorTransacao.atualizaEComita(proposta);
        });
    }

}
