package com.dtalks.dtalks.user.controller;

import com.dtalks.dtalks.user.dto.DuplicateResponseDto;
import com.dtalks.dtalks.user.dto.SignInResponseDto;
import com.dtalks.dtalks.user.dto.UserResponseDto;
import com.dtalks.dtalks.user.entity.User;
import com.dtalks.dtalks.user.service.UserDetailsService;
import com.dtalks.dtalks.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @Autowired
    public UserController(UserDetailsService userDetailsService, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @GetMapping(value = "/check/{userid}")
    public ResponseEntity<DuplicateResponseDto> useridDuplicateCheck(@PathVariable String userid) {
        DuplicateResponseDto duplicateResponseDto = userService.useridDuplicated(userid);
        return ResponseEntity.ok(duplicateResponseDto);
    }

    @GetMapping(value = "/check/{nickname}")
    public ResponseEntity<DuplicateResponseDto> nicknameDuplicatedCheck(@PathVariable String nickname) {
        DuplicateResponseDto duplicateResponseDto = userService.nicknameDuplicated(nickname);
        return  ResponseEntity.ok(duplicateResponseDto);
    }

    @GetMapping(value = "/info")
    public ResponseEntity<UserResponseDto> userInformation(@AuthenticationPrincipal User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        LOGGER.info("test" + user);
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setNickname(user.getNickname());
        userResponseDto.setUserid(user.getUsername());
        userResponseDto.setRegistrationId(user.getRegistrationId());
        return ResponseEntity.ok(userResponseDto);
    }

    @PostMapping(value = "token/refresh")
    public ResponseEntity<SignInResponseDto> tokenRefresh(String refreshToken) {
        SignInResponseDto signInResponseDto = userService.reSignIn(refreshToken);

        return ResponseEntity.ok(signInResponseDto);
    }
}
