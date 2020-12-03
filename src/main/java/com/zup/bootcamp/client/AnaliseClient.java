package com.zup.bootcamp.client;

import com.zup.bootcamp.client.request.SolicitacaoAnaliseRequest;
import com.zup.bootcamp.client.response.ResultadoAnaliseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "analise", url = "${analisePropostaClient.host}")
public interface AnaliseClient {

    @RequestMapping(method = RequestMethod.POST, value = "/solicitacao")
    public ResultadoAnaliseResponse solicitacaoAnalise(@RequestBody SolicitacaoAnaliseRequest analiseRequest);
}
