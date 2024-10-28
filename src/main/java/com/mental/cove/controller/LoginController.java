package com.mental.cove.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.mental.cove.service.JwtTokenProvider;
import com.mental.cove.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
public class LoginController {

    @Resource
    private WxMaService wxMaService;

    @Resource
    private UserService userService;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String code) {
        try {
//            String openid = wxMaService.getUserService().getSessionInfo(code).getOpenid();
//            String accessToken = wxMaService.getAccessToken();
            String openid = UUID.randomUUID().toString();
            String accessToken = UUID.randomUUID().toString();
            userService.getOrCreateUser(openid);
            String token = jwtTokenProvider.createToken(openid, accessToken);
            return ResponseEntity.ok().body(token);
        } catch (Exception e) {
            log.error("Login failed for code:{}", code, e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to authenticate with WeChat");
        }

    }
}
