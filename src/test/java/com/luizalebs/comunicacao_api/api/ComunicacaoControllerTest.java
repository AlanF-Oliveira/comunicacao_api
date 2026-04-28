package com.luizalebs.comunicacao_api.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTOFixture;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTOFixture;
import com.luizalebs.comunicacao_api.business.mapper.ComunicacaoConverter;
import com.luizalebs.comunicacao_api.business.service.ComunicacaoService;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.ConflictException;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.GlobalExceptionHandler;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ComunicacaoControllerTest {
    @InjectMocks
    ComunicacaoController comunicacaoController;

    @Mock
    ComunicacaoService comunicacaoService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    ComunicacaoInDTO comunicacaoInDTO;
    ComunicacaoOutDTO comunicacaoOutDTO;
    ComunicacaoConverter comunicacaoConverter;
    private String json;
    private String url;
    private final Date dataHora = Date.from(
            LocalDateTime.of(2026, 4, 24, 13, 56, 20)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
    );
    String email;
    MockMvc mockMvc;

    @BeforeEach
    void setup() throws JsonProcessingException {
        url = "/comunicacao";
        mockMvc = MockMvcBuilders
                .standaloneSetup(comunicacaoController)
                .setControllerAdvice(new GlobalExceptionHandler()) // ← adiciona isso
                .alwaysDo(print())
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
        email = "alanf@gmail.com";
        json = objectMapper.writeValueAsString(comunicacaoInDTO);
    }

    @Test
    void deveAgendarUsuarioComSucesso() throws Exception {
        when(comunicacaoService.agendarComunicacao(comunicacaoInDTO))
                .thenReturn(comunicacaoOutDTO);
        mockMvc.perform(post("/comunicacao/agendar")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());
        verify(comunicacaoService).agendarComunicacao(comunicacaoInDTO);
        verifyNoMoreInteractions(comunicacaoService);
    }

    @Test
    void naoDeveAgendarUsuarioCasoJsonNull() throws Exception {
        mockMvc.perform(post("/comunicacao/agendar")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
        verifyNoInteractions(comunicacaoService);
    }

    @Test
    void naoDeveAgenfarCasoEmailExistente() throws Exception {
        when(comunicacaoService.agendarComunicacao(comunicacaoInDTO))
                .thenThrow(new ConflictException("Já existe uma mensagem com este e-mail."));
        mockMvc.perform(post("/comunicacao/agendar")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isConflict());
        verify(comunicacaoService).agendarComunicacao(comunicacaoInDTO);
        verifyNoMoreInteractions(comunicacaoService);
    }

    @Test
    void naoDeveBuscarStatusDaComunicacaoCasoEmailInexistente() throws Exception {
        when(comunicacaoService.buscarStatusComunicacao(email))
                .thenThrow(new ResourceNotFoundException("Mensagem não encontrada."));
        mockMvc.perform(get("/comunicacao")
                .param("emailDestinatario", "alanf@gmail.com")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
        verify(comunicacaoService).buscarStatusComunicacao(email);
        verifyNoMoreInteractions(comunicacaoService);
    }

    @Test
    void deveBuscarStatusComunicacaoComSucesso() throws Exception {
        when(comunicacaoService.buscarStatusComunicacao(email))
                .thenReturn(comunicacaoOutDTO);
        mockMvc.perform(get("/comunicacao")
                .param("emailDestinatario", "alanf@gmail.com")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        verify(comunicacaoService).buscarStatusComunicacao(email);
        verifyNoMoreInteractions(comunicacaoService);
    }

    @Test
    void naoDeveCancelarStatusDaComunicacaoCasoEmailInexistente() throws Exception {
        when(comunicacaoService.alterarStatusComunicacao(email))
                .thenThrow(new ResourceNotFoundException("Mensagem não encontrada."));
        mockMvc.perform(patch("/comunicacao/cancelar")
                .param("emailDestinatario", "alanf@gmail.com")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
        verify(comunicacaoService).alterarStatusComunicacao(email);
        verifyNoMoreInteractions(comunicacaoService);
    }

    @Test
    void deveCancelarStatusDaComunicacaoComSucesso() throws Exception {
        when(comunicacaoService.alterarStatusComunicacao(email))
                .thenReturn(comunicacaoOutDTO);
        mockMvc.perform(patch("/comunicacao/cancelar")
                .param("emailDestinatario", "alanf@gmail.com")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        verify(comunicacaoService).alterarStatusComunicacao(email);
        verifyNoMoreInteractions(comunicacaoService);
    }



}