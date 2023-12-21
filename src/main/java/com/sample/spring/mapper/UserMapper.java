package com.sample.spring.mapper;

import com.sample.spring.dto.UserDto;
import com.sample.spring.entity.UserEntity;
import com.sample.spring.web.vo.UserRequestVo;
import com.sample.spring.web.vo.UserResponseVo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto entityToDto(UserEntity entity);

    UserEntity dtoToEntity(UserDto dto);

    UserDto voToDto(UserRequestVo vo);

    UserResponseVo dtoToVo(UserDto dto);

    void entityToEntity(UserEntity entityA, @MappingTarget UserEntity entityB);
}
