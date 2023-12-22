package com.sample.spring.web;

import com.sample.spring.common.model.ResponseVO;
import com.sample.spring.common.model.ResponseVOBuilder;
import com.sample.spring.dto.UserDto;
import com.sample.spring.entity.UserEntity;
import com.sample.spring.mapper.UserMapper;
import com.sample.spring.service.UserService;
import com.sample.spring.web.vo.UserRequestVo;
import com.sample.spring.web.vo.UserResponseVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    @GetMapping
    public ResponseVO<UserResponseVo> getUserByUsername(@RequestParam String username) {
        UserDetails userDetails = this.service.loadUserByUsername(username);
        UserResponseVo responseVo = new UserResponseVo();
        if (userDetails instanceof UserEntity) {
            UserEntity user = (UserEntity) userDetails;
            responseVo = new UserResponseVo(user.getId(), user.getUsername(), user.getStatus());
        }
        return new ResponseVOBuilder<UserResponseVo>().addData(responseVo).build();
    }

    @PostMapping
    public ResponseVO<UserResponseVo> save(@RequestBody UserRequestVo vo) {
        UserDto dto = UserMapper.INSTANCE.voToDto(vo);
        dto = this.service.save(dto);
        UserResponseVo responseVo = UserMapper.INSTANCE.dtoToVo(dto);
        return new ResponseVOBuilder<UserResponseVo>().addData(responseVo).build();
    }

}
