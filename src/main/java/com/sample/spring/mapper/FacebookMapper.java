package com.sample.spring.mapper;

import com.sample.spring.dto.FacebookRequestDto;
import com.sample.spring.web.vo.FacebookRequestVo;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FacebookMapper {
    FacebookMapper INSTANCE = Mappers.getMapper(FacebookMapper.class);
    FacebookRequestDto voToDto(FacebookRequestVo vo);
}
