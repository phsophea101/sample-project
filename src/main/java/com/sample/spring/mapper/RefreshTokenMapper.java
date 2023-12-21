package com.sample.spring.mapper;

import com.sample.spring.dto.RefreshTokenDto;
import com.sample.spring.entity.RefreshTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RefreshTokenMapper {
    RefreshTokenMapper INSTANCE = Mappers.getMapper(RefreshTokenMapper.class);

    RefreshTokenDto entityToDto(RefreshTokenEntity entity);

    RefreshTokenEntity dtoToEntity(RefreshTokenDto refreshTokenDto);
}
