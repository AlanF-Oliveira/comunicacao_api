package com.luizalebs.comunicacao_api.business.service;


import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.infraestructure.client.EmailClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailClient client;

    public void enviarMensagem (ComunicacaoOutDTO dto) {
        client.enviarMensagem(dto);
    }

}
