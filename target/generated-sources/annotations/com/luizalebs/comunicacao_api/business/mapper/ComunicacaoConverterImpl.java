package com.luizalebs.comunicacao_api.business.mapper;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-28T17:39:43-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 11.0.30 (Amazon.com Inc.)"
)
@Component
public class ComunicacaoConverterImpl implements ComunicacaoConverter {

    @Override
    public ComunicacaoEntity paraEntity(ComunicacaoInDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ComunicacaoEntity.ComunicacaoEntityBuilder comunicacaoEntity = ComunicacaoEntity.builder();

        comunicacaoEntity.dataHoraenvio( dto.getDataHoraEnvio() );
        comunicacaoEntity.nomeDestinatario( dto.getNomeDestinatario() );
        comunicacaoEntity.emailDestinatario( dto.getEmailDestinatario() );
        comunicacaoEntity.telefoneDestinatario( dto.getTelefoneDestinatario() );
        comunicacaoEntity.mensagem( dto.getMensagem() );
        comunicacaoEntity.modoDeEnvio( dto.getModoDeEnvio() );
        comunicacaoEntity.statusEnvio( dto.getStatusEnvio() );

        return comunicacaoEntity.build();
    }

    @Override
    public ComunicacaoOutDTO paraDTO(ComunicacaoEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ComunicacaoOutDTO.ComunicacaoOutDTOBuilder comunicacaoOutDTO = ComunicacaoOutDTO.builder();

        comunicacaoOutDTO.dataHoraEnvio( entity.getDataHoraenvio() );
        comunicacaoOutDTO.nomeDestinatario( entity.getNomeDestinatario() );
        comunicacaoOutDTO.emailDestinatario( entity.getEmailDestinatario() );
        comunicacaoOutDTO.telefoneDestinatario( entity.getTelefoneDestinatario() );
        comunicacaoOutDTO.mensagem( entity.getMensagem() );
        comunicacaoOutDTO.modoDeEnvio( entity.getModoDeEnvio() );
        comunicacaoOutDTO.statusEnvio( entity.getStatusEnvio() );

        return comunicacaoOutDTO.build();
    }

    @Override
    public List<ComunicacaoOutDTO> paraListaDTO(List<ComunicacaoEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ComunicacaoOutDTO> list = new ArrayList<ComunicacaoOutDTO>( entities.size() );
        for ( ComunicacaoEntity comunicacaoEntity : entities ) {
            list.add( paraDTO( comunicacaoEntity ) );
        }

        return list;
    }
}
