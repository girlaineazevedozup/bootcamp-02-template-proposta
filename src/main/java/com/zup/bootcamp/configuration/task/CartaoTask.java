package com.zup.bootcamp.configuration.task;

import com.zup.bootcamp.client.CartaoClient;
import com.zup.bootcamp.client.response.CartaoResponse;
import com.zup.bootcamp.infrastructure.ExecutorTransacao;
import com.zup.bootcamp.infrastructure.PropostaRepository;
import com.zup.bootcamp.model.Cartao;
import com.zup.bootcamp.model.Proposta;
import com.zup.bootcamp.model.enums.StatusProposta;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class CartaoTask {

    private final Logger logger = LoggerFactory.getLogger(CartaoTask.class);

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private ExecutorTransacao executorTransacao;

    @Autowired
    private CartaoClient cartaoClient;

    @Scheduled(fixedDelayString = "${periodicidade.associa-cartao}")
    @Transactional
    protected void associaCartao() {

        List<Proposta> listaProposta = propostaRepository.findByStatusAndCartaoNull(StatusProposta.ELEGIVEL);

        listaProposta.forEach(proposta -> {
            Cartao cartao = solicitaCartao(proposta);
            if(cartao != null){
                proposta.setCartao(cartao);
                executorTransacao.atualizaEComita(proposta);
            }
        });
    }

    private Cartao solicitaCartao(Proposta proposta){
        CartaoResponse response;
        try{
            response = cartaoClient.solicitacaoCartao(proposta.getId());
        }catch (FeignException ex){
            logger.error("Serviço de Solicitação de Cartão indisponível!", ex);
            return null;
        }
        Cartao cartao = new Cartao(response.getId());
        executorTransacao.salvaEComita(cartao);

        return cartao;
    }
}
