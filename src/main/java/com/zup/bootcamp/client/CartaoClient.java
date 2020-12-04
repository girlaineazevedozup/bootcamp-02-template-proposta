package com.zup.bootcamp.client;

import com.zup.bootcamp.client.response.CartaoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "cartao", url = "${cartaoClient.host}")
public interface CartaoClient {

    @RequestMapping(method = RequestMethod.GET, value = "/cartoes")
    public CartaoResponse solicitacaoCartao(@RequestParam("idProposta") Long idProposta);
}
