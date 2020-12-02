package com.zup.bootcamp.client;

import com.zup.bootcamp.request.SolicitacaoAnaliseRequest;
import com.zup.bootcamp.response.ResultadoAnaliseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "analise", url = "${analiseClient.host}")
public interface AnaliseClient {

    @RequestMapping(method = RequestMethod.POST, value = "/solicitacao")
    ResultadoAnaliseResponse solicitacaoAnalise(@RequestBody SolicitacaoAnaliseRequest analiseRequest);
}
