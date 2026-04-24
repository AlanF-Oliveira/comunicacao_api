package com.luizalebs.comunicacao_api.business.mapper;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@ExtendWith({MockitoExtension.class})
public class ComunicacaoConverterTest {
    @InjectMocks
    ComunicacaoConverter comunicacaoConverter;

    ComunicacaoEntity comunicacaoEntity;
    ComunicacaoInDTO comunicacaoInDTO;
    ComunicacaoOutDTO comunicacaoOutDTO;
    Date dataHora = Date.from(
            LocalDateTime.of(2026, 4, 24, 13, 56, 20)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
    );
    ModoEnvioEnum envioEnum;

    @BeforeEach
    void setup() {
        comunicacaoEntity = ComunicacaoEntity.builder()
                .dataHoraenvio(dataHora)
                .nomeDestinatario("Alan Ferreira de Oliveira")
                .emailDestinatario("alanf@gmail.com")
                .telefoneDestinatario("85986546543")
                .mensagem("Testes unitários")
                .modoDeEnvio(ModoEnvioEnum.EMAIL)
                .build();
    }
}
