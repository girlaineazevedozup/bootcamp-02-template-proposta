package com.zup.bootcamp.configuration.task;

import com.zup.bootcamp.client.CartaoClient;
import com.zup.bootcamp.client.response.CartaoResponse;
import com.zup.bootcamp.infrastructure.CartaoRepository;
import com.zup.bootcamp.infrastructure.ExecutorTransacao;
import com.zup.bootcamp.infrastructure.PropostaRepository;
import com.zup.bootcamp.model.Cartao;
import com.zup.bootcamp.model.Proposta;
import com.zup.bootcamp.model.StatusProposta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class CartaoTask {

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private ExecutorTransacao executorTransacao;

    @Autowired
    private CartaoClient cartaoClient;

    @Scheduled(fixedDelayString = "${periodicidade.associa-cartao}")
    @Transactional
    private void associaCartao() {

        List<Proposta> listaProposta = propostaRepository.findByStatusAndCartaoNull(StatusProposta.ELEGIVEL);

        listaProposta.forEach(proposta -> {
            CartaoResponse response = cartaoClient.solicitacaoCartao(proposta.getId());

            Cartao cartao = new Cartao(response.getId());
            executorTransacao.salvaEComita(cartao);

            proposta.setCartao(cartao);
            executorTransacao.atualizaEComita(proposta);
        });
    }

}
