package com.sample.spring.service.impl;

import com.sample.spring.common.exception.BizException;
import com.sample.spring.dto.UserDto;
import com.sample.spring.entity.UserEntity;
import com.sample.spring.enums.BizErrorCode;
import com.sample.spring.mapper.UserMapper;
import com.sample.spring.repository.UserRepository;
import com.sample.spring.service.UserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserService {
    private final UserRepository userRepository;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = this.userRepository.findByUsername(username);
        if (ObjectUtils.isEmpty(entity))
            throw new BizException(BizErrorCode.E0002);
        return entity;
    }

    @SneakyThrows
    @Override
    public UserDto save(UserDto dto) {
        UserEntity entity = this.userRepository.findByUsername(dto.getUsername());
        if (ObjectUtils.isNotEmpty(entity))
            throw new BizException(BizErrorCode.E0003, String.format("This username %s %s", dto.getUsername(), "already existed."));
        entity = UserMapper.INSTANCE.dtoToEntity(dto);
        this.userRepository.save(entity);
        return UserMapper.INSTANCE.entityToDto(entity);
    }
}
