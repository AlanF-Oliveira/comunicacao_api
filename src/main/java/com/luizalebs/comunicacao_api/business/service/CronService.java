package com.luizalebs.comunicacao_api.business.service;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class CronService {

    private final EmailService emailService;
    private final ComunicacaoService comunicacaoService;


    @Scheduled(cron = "${cron.horario}")
    public void buscarPorMensagensPendentes() {
        log.info("Iniciada a busca de mensagens");
        List<ComunicacaoOutDTO> listaDeMensagensPendentes = comunicacaoService.buscarMensagensPendentes();

        log.info("mensagens encontadas: " + listaDeMensagensPendentes);
        listaDeMensagensPendentes.forEach(mensagem -> {
            emailService.enviarMensagem(mensagem);
            comunicacaoService.marcarComoEnviado(mensagem.getEmailDestinatario());
            log.info("Mensagem enviada para o usuário "+ mensagem.getEmailDestinatario());
        });
        log.info("Finalizado a busca e notificacao de mensagens");
    }



}
