package com.sample.spring.service;

import com.sample.spring.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto save(UserDto dto);
}
