package com.luizalebs.comunicacao_api.business.service;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTOFixture;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CronServiceTest {
    @InjectMocks
    CronService cronService;

    @Mock
    ComunicacaoService comunicacaoService;

    @Mock
    EmailService emailService;

    ComunicacaoOutDTO comunicacaoOutDTO;
    private final Date dataHora = Date.from(
            LocalDateTime.of(2026, 4, 24, 13, 56, 20)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
    );

    @BeforeEach
    void setup() {
        comunicacaoOutDTO = ComunicacaoOutDTOFixture.build(
                dataHora,
                "Alan Ferreira de Oliveira",
                "alanf@gmail.com",
                "85986546543",
                "Testes unitários",
                ModoEnvioEnum.EMAIL,
                StatusEnvioEnum.PENDENTE
        );
    }

    @Test
    void deveBuscarPorMensagensPendentes() {
        when(comunicacaoService.buscarMensagensPendentes()).thenReturn(List.of(comunicacaoOutDTO));
        cronService.buscarPorMensagensPendentes();
        verify(comunicacaoService).buscarMensagensPendentes();
        verify(emailService).enviarMensagem(comunicacaoOutDTO);
        verify(comunicacaoService).marcarComoEnviado(comunicacaoOutDTO.getEmailDestinatario());
        verifyNoMoreInteractions(emailService, comunicacaoService);
    }
}
