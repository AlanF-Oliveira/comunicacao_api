package com.luizalebs.comunicacao_api.business.mapper;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ComunicacaoConverterTest {

    ComunicacaoConverter comunicacaoConverter;
    ComunicacaoEntity comunicacaoEntity;
    ComunicacaoInDTO comunicacaoInDTO;
    ComunicacaoOutDTO comunicacaoOutDTO;
    ModoEnvioEnum envioEnum;
    StatusEnvioEnum statusEnvioEnum;
    Date dataHora = Date.from(
            LocalDateTime.of(2026, 4, 24, 13, 56, 20)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
    );


    @BeforeEach
    void setup() {
        comunicacaoConverter = Mappers.getMapper(ComunicacaoConverter.class);
        comunicacaoEntity = ComunicacaoEntity.builder()
                .dataHoraenvio(dataHora)
                .nomeDestinatario("Alan Ferreira de Oliveira")
                .emailDestinatario("alanf@gmail.com")
                .telefoneDestinatario("85986546543")
                .mensagem("Testes unitários")
                .modoDeEnvio(ModoEnvioEnum.EMAIL)
                .statusEnvio(StatusEnvioEnum.PENDENTE)
                .build();
        comunicacaoInDTO = ComunicacaoInDTO.builder()
                .dataHoraEnvio(dataHora)
                .nomeDestinatario("Alan Ferreira de Oliveira")
                .emailDestinatario("alanf@gmail.com")
                .telefoneDestinatario("85986546543")
                .mensagem("Testes unitários")
                .modoDeEnvio(ModoEnvioEnum.EMAIL)
                .statusEnvio(StatusEnvioEnum.PENDENTE)
                .build();
        comunicacaoOutDTO = ComunicacaoOutDTO.builder()
                .dataHoraEnvio(dataHora)
                .nomeDestinatario("Alan Ferreira de Oliveira")
                .emailDestinatario("alanf@gmail.com")
                .telefoneDestinatario("85986546543")
                .mensagem("Testes unitários")
                .modoDeEnvio(ModoEnvioEnum.EMAIL)
                .statusEnvio(StatusEnvioEnum.ENVIADO)
                .build();

    }

    @Test
    void deveConverterParaComunicacaoEntity() {
        ComunicacaoEntity entity = comunicacaoConverter.paraEntity(comunicacaoInDTO);
        assertEquals(comunicacaoEntity, entity);
    }

}
