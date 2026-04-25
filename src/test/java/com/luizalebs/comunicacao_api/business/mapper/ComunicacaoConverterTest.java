package com.luizalebs.comunicacao_api.business.mapper;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTOFixture;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTOFixture;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ComunicacaoConverterTest {

    ComunicacaoConverter comunicacaoConverter;
    ComunicacaoEntity comunicacaoEntity;
    ComunicacaoInDTO comunicacaoInDTO;
    ComunicacaoOutDTO comunicacaoOutDTO;
    List <ComunicacaoEntity> entities;
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
        comunicacaoInDTO = ComunicacaoInDTOFixture.build(dataHora,
                "Alan Ferreira de Oliveira",
                "alanf@gmail.com",
                "85986546543",
                "Testes unitários",
                ModoEnvioEnum.EMAIL,
                StatusEnvioEnum.PENDENTE
        );
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
    void deveConverterParaComunicacaoEntity() {
        ComunicacaoEntity entity = comunicacaoConverter.paraEntity(comunicacaoInDTO);
        assertEquals(comunicacaoEntity, entity);
    }

    @Test
    void deveConverterParaDTO() {
        ComunicacaoOutDTO dto = comunicacaoConverter.paraDTO(comunicacaoEntity);
        assertEquals(comunicacaoOutDTO, dto);
    }

    @Test
    void deveConverterParaListaDTO(){
        List<ComunicacaoEntity> listaEntity = List.of(comunicacaoEntity);
        List<ComunicacaoOutDTO> listaDTO = comunicacaoConverter.paraListaDTO(listaEntity);
        assertEquals(1, listaDTO.size());
        assertEquals(comunicacaoOutDTO, listaDTO.get(0));
    }

}
