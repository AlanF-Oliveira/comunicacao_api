package com.luizalebs.comunicacao_api.business.mapper;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComunicacaoConverter {

    @Mapping(source = "dataHoraEnvio", target = "dataHoraenvio")
    ComunicacaoEntity paraEntity(ComunicacaoInDTO dto);

    @Mapping(source = "dataHoraenvio", target = "dataHoraEnvio")
    ComunicacaoOutDTO paraDTO(ComunicacaoEntity entity);


    List<ComunicacaoOutDTO> paraListaDTO(List<ComunicacaoEntity> entities);
}
