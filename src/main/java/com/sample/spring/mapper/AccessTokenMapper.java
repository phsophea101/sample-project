package com.sample.spring.mapper;

import com.sample.spring.dto.AccessTokenDto;
import com.sample.spring.entity.AccessTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccessTokenMapper {
    AccessTokenMapper INSTANCE = Mappers.getMapper(AccessTokenMapper.class);

    AccessTokenDto entityToDto(AccessTokenEntity entity);

    AccessTokenEntity dtoToEntity(AccessTokenDto dto);

    List<AccessTokenDto> entityToDtoList(List<AccessTokenEntity> entities);

    void entityToEntity(AccessTokenEntity entityA, @MappingTarget AccessTokenEntity entityB);
}
