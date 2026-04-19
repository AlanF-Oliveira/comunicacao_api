package com.luizalebs.comunicacao_api.infraestructure.client;


import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificacao", url = "${notificacao.url}")
public interface EmailClient {

    @PostMapping
    void enviarMensagem(@RequestBody ComunicacaoInDTO dto);
}
