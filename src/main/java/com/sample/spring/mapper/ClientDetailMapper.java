package com.sample.spring.mapper;

import com.sample.spring.dto.ClientDetailDto;
import com.sample.spring.entity.ClientDetailEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientDetailMapper {
    ClientDetailMapper INSTANCE = Mappers.getMapper(ClientDetailMapper.class);

    BaseClientDetails entityToDto(ClientDetailEntity entity);

    ClientDetailDto to(ClientDetailEntity entity);

}
