package com.luizalebs.comunicacao_api.business.service;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTOFixture;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class CronServiceTest {
    @InjectMocks
    CronService cronService;

    @Mock
    ComunicacaoService comunicacaoService;

    ComunicacaoOutDTO comunicacaoOutDTO;
    private final Date dataHora = Date.from(
            LocalDateTime.of(2026, 4, 24, 13, 56, 20)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
    );
    String email;

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
        email = "alanf@gmail.com";
    }

}
