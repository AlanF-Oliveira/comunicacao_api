package com.luizalebs.comunicacao_api.business.service;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTOFixture;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTOFixture;
import com.luizalebs.comunicacao_api.business.mapper.ComunicacaoConverter;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.BusinessException;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.ConflictException;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.ResourceNotFoundException;
import com.luizalebs.comunicacao_api.infraestructure.repositories.ComunicacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComunicacaoServiceTest {
    @InjectMocks
    ComunicacaoService comunicacaoService;

    @Mock
    ComunicacaoRepository comunicacaoRepository;

    @Mock
    ComunicacaoConverter comunicacaoConverter;

    ComunicacaoEntity comunicacaoEntity;
    ComunicacaoInDTO comunicacaoInDTO;
    ComunicacaoOutDTO comunicacaoOutDTO;
    private final Date dataHora = Date.from(
            LocalDateTime.of(2026, 4, 24, 13, 56, 20)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
    );
    String email;

    @BeforeEach
    void setup() {
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
        email = "alanf@gmail.com";
    }

    @Test
    void naoDeveSalvarDTONulo() {
        BusinessException e = assertThrows(BusinessException.class,
                () -> comunicacaoService.agendarComunicacao(null));
        assertThat(e.getMessage(), is("Dados da mensagem inválidos."));
        verifyNoInteractions(comunicacaoConverter, comunicacaoRepository);
    }

    @Test
    void naoDeveSalvarCasoEmailExistente(){
        when(comunicacaoRepository.existsByEmailDestinatario(comunicacaoInDTO.getEmailDestinatario())).thenReturn(true);
        ConflictException e  = assertThrows(ConflictException.class,
        () ->comunicacaoService.agendarComunicacao(comunicacaoInDTO));
        assertThat(e.getMessage(), is("Já existe uma mensagem com este e-mail."));
        verify(comunicacaoRepository).existsByEmailDestinatario(comunicacaoInDTO.getEmailDestinatario());
        verifyNoInteractions(comunicacaoConverter);

    }

    @Test
    void deveAgendarComunicacao() {
        when(comunicacaoRepository.existsByEmailDestinatario(comunicacaoInDTO.getEmailDestinatario())).thenReturn(false);
        when(comunicacaoConverter.paraEntity(comunicacaoInDTO)).thenReturn(comunicacaoEntity);
        when(comunicacaoRepository.save(comunicacaoEntity)).thenReturn(comunicacaoEntity);
        when(comunicacaoConverter.paraDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTO);
        ComunicacaoOutDTO dto = comunicacaoService.agendarComunicacao(comunicacaoInDTO);
        assertEquals(comunicacaoOutDTO, dto);
        verify(comunicacaoRepository).existsByEmailDestinatario(comunicacaoInDTO.getEmailDestinatario());
        verify(comunicacaoConverter).paraEntity(comunicacaoInDTO);
        verify(comunicacaoRepository).save(comunicacaoEntity);
        verify(comunicacaoConverter).paraDTO(comunicacaoEntity);
        verifyNoMoreInteractions(comunicacaoRepository, comunicacaoConverter);
    }

    @Test
    void naoDeveBuscarCasoEmailNull() {
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> comunicacaoService.buscarStatusComunicacao(null));
        assertThat(e.getMessage(), is("Mensagem não encontrada."));
        verifyNoInteractions(comunicacaoConverter);
    }

    @Test
    void deveBuscarStatusComunicacao(){
        when(comunicacaoRepository.findByEmailDestinatario(email)).thenReturn(comunicacaoEntity);
        when(comunicacaoConverter.paraDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTO);
        ComunicacaoOutDTO dto = comunicacaoService.buscarStatusComunicacao(email);
        assertEquals(comunicacaoOutDTO, dto);
        verify(comunicacaoRepository).findByEmailDestinatario(email);
        verify(comunicacaoConverter).paraDTO(comunicacaoEntity);
        verifyNoMoreInteractions(comunicacaoRepository, comunicacaoConverter);
    }

    @Test
    void naoDeveaAlterarStatusComunicacaoCasoEmailNulo() {
        when(comunicacaoRepository.findByEmailDestinatario(null)).thenReturn(null);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> comunicacaoService.alterarStatusComunicacao(null));
        assertThat(e.getMessage(), is("Mensagem não encontrada."));
        verify(comunicacaoRepository).findByEmailDestinatario(null);
        verifyNoInteractions(comunicacaoConverter);
    }

    @Test
    void deveAlterarStatusComunicacao(){
        when(comunicacaoRepository.findByEmailDestinatario(email)).thenReturn(comunicacaoEntity);
        when(comunicacaoRepository.save(comunicacaoEntity)).thenReturn(comunicacaoEntity);
        when(comunicacaoConverter.paraDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTO);
        ComunicacaoOutDTO dto = comunicacaoService.alterarStatusComunicacao(email);
        assertEquals(StatusEnvioEnum.CANCELADO, comunicacaoEntity.getStatusEnvio());
        assertEquals(comunicacaoOutDTO, dto);
        verify(comunicacaoRepository).findByEmailDestinatario(email);
        verify(comunicacaoRepository).save(comunicacaoEntity);
        verify(comunicacaoConverter).paraDTO(comunicacaoEntity);
        verifyNoMoreInteractions(comunicacaoConverter, comunicacaoRepository);
    }
}
