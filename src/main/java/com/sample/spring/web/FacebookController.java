package com.sample.spring.web;

import com.sample.spring.common.model.ResponseVO;
import com.sample.spring.common.model.ResponseVOBuilder;
import com.sample.spring.dto.FacebookRequestDto;
import com.sample.spring.mapper.FacebookMapper;
import com.sample.spring.service.FacebookService;
import com.sample.spring.web.vo.FacebookRequestVo;
import com.sample.spring.web.vo.FacebookResponseVo;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/facebook")
public class FacebookController {
    @Autowired
    private FacebookService service;
    @GetMapping
    @SneakyThrows
    public ResponseVO<FacebookResponseVo> getService(FacebookRequestVo vo) {
        FacebookRequestDto dto = FacebookMapper.INSTANCE.voToDto(vo);
        service.getPost(dto);
        FacebookResponseVo responseVo = new FacebookResponseVo();
        return new ResponseVOBuilder<FacebookResponseVo>().addData(responseVo).build();
    }

}
