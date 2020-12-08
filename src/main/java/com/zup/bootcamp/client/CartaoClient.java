package com.zup.bootcamp.client;

import com.zup.bootcamp.client.request.BloqueioRequest;
import com.zup.bootcamp.client.response.BloqueioResponse;
import com.zup.bootcamp.client.response.CartaoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@FeignClient(value = "cartao", url = "${cartaoClient.host}")
public interface CartaoClient {

    @GetMapping("/cartoes")
    public CartaoResponse solicitacaoCartao(@RequestParam("idProposta") UUID idProposta);

    @PostMapping("/cartoes/{id}/bloqueios")
    public BloqueioResponse bloqueiaCartao(@PathVariable("id") String id,
                                           @RequestBody @Valid BloqueioRequest bloqueioRequest);

}
