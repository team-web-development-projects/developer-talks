package com.dtalks.dtalks.user.controller;

import com.dtalks.dtalks.base.dto.DocumentResponseDto;
import com.dtalks.dtalks.studyroom.enums.Skill;
import com.dtalks.dtalks.user.dto.*;
import com.dtalks.dtalks.user.entity.User;
import com.dtalks.dtalks.user.service.UserDetailsService;
import com.dtalks.dtalks.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Tag(name = "users")
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

    @Operation(summary = "userid 중복 체크")
    @GetMapping(value = "/check/userid/{userid}")
    public ResponseEntity<DuplicateResponseDto> useridDuplicateCheck(@PathVariable String userid) {
        DuplicateResponseDto duplicateResponseDto = userService.useridDuplicated(userid);
        return ResponseEntity.ok(duplicateResponseDto);
    }

    @Operation(summary = "nickname 중복 체크")
    @GetMapping(value = "/check/nickname/{nickname}")
    public ResponseEntity<DuplicateResponseDto> nicknameDuplicatedCheck(@PathVariable String nickname) {
        DuplicateResponseDto duplicateResponseDto = userService.nicknameDuplicated(nickname);
        return ResponseEntity.ok(duplicateResponseDto);
    }

    @Operation(summary = "email 중복체크")
    @GetMapping(value = "/check/email/{email}")
    public ResponseEntity<DuplicateResponseDto> emailDuplicatedCheck(@PathVariable String email) {
        return ResponseEntity.ok(userService.emailDuplicated(email));
    }


    @Operation(summary = "유저 정보")
    @GetMapping(value = "/info")
    public ResponseEntity<UserResponseDto> userInformation(@AuthenticationPrincipal User user) {
        LOGGER.info("user info controller");
        UserResponseDto userResponseDto = userService.userInfo();
        return ResponseEntity.ok(userResponseDto);
    }

    @Operation(summary = "프로필 이미지 업로드")
    @PostMapping(value = "/profile/image"
    , consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DocumentResponseDto> profileImageUpLoad(@RequestPart("file") MultipartFile file) {
        LOGGER.info("profileImageUpLoad controller 호출됨");
        return ResponseEntity.ok(userService.userProfileImageUpLoad(file));
    }

    @Operation(summary = "프로필 이미지 조회")
    @GetMapping(value = "/profile/image")
    public ResponseEntity<DocumentResponseDto> getUserProfileImage() {
        return ResponseEntity.ok(userService.getUserProfileImage());
    }

    @Operation(summary = "프로필 이미지 수정(기존 이미지 없어도 됨)")
    @PutMapping(value = "/profile/image"
    , consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DocumentResponseDto> updateProfileImage(@RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(userService.updateUserProfileImage(file));
    }

    @Operation(summary = "유저 소개글, 기술스택 수정")
    @PutMapping(value = "/profile")
    public ResponseEntity<UserResponseDto> updateUserProfile(@RequestBody UserProfileRequestDto userProfileRequestDto) {
        LOGGER.info("updateUserDescription controller 호출됨");
        return ResponseEntity.ok(userService.updateUserProfile(userProfileRequestDto));
    }

    @GetMapping(value = "/recent/activity/{nickname}")
    @Operation(summary = "특정 유저의 최근활동 조회 (페이지 사용, size = 10, sort=\"createDate\" desc 적용)", parameters = {
            @Parameter(name = "nickname", description = "조회할 유저의 닉네임")
    })
    public ResponseEntity<Page<RecentActivityDto>> getRecentActivities(@PathVariable String nickname,
                                                                       @PageableDefault(size = 10, sort = "createDate",  direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(userService.getRecentActivities(nickname, pageable));

    }

    @Operation(summary = "특정 유저의 비공개 여부 조회", parameters = {
            @Parameter(name = "id", description = "조회할 유저의 로그인 아이디"),
    })
    @GetMapping(value = "/private/{id}")
    public ResponseEntity<Boolean> getPrivateStatus(@PathVariable String id) {
        return ResponseEntity.ok(userService.getPrivateStatus(id));
    }

    @Operation(summary = "특정 유저의 비공개 여부 설정", parameters = {
            @Parameter(name = "status", description = "비공개 설정 true/false")
    })
    @PutMapping(value = "/setting/private/{status}")
    public void updatePrivate(@PathVariable boolean status) {
        userService.updatePrivate(status);
    }

    @Operation(summary = "유저 닉네임 변경")
    @PutMapping(value = "/profile/nickname")
    public ResponseEntity<SignInResponseDto> updateNickname(@RequestBody String nickname) {
        return ResponseEntity.ok(userService.updateNickname(nickname));
    }

    @Operation(summary = "유저 아이디 변경")
    @PutMapping(value = "/profile/userid")
    public ResponseEntity<SignInResponseDto> updateUserid(
            @RequestBody @Valid UseridDto useridDto
    ) {
        return ResponseEntity.ok(userService.updateUserid(useridDto));
    }

    @Operation(summary = "유저 비밀번호 변경")
    @PutMapping(value = "/profile/password")
    public ResponseEntity<SignInResponseDto> updateUserPassword(
            @RequestBody @Valid UserPasswordDto userPasswordDto
    ) {
        return ResponseEntity.ok(userService.updateUserPassword(userPasswordDto));
    }

    @Operation(summary = "유저 이메일 변경")
    @PutMapping(value = "/profle/email")
    public ResponseEntity<SignInResponseDto> updateUserEmail(
            @RequestBody @Valid UserEmailDto userEmailDto
    ) {
        return ResponseEntity.ok(userService.updateUserEmail(userEmailDto));
    }
}
