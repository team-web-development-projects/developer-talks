package com.dtalks.dtalks.admin.user.controller;

import com.dtalks.dtalks.admin.user.dto.UserInfoChangeRequestDto;
import com.dtalks.dtalks.admin.user.dto.UserManageDto;
import com.dtalks.dtalks.admin.user.service.UserManageService;
import com.dtalks.dtalks.user.enums.ActiveStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/user")
public class AdminUserController {

    private final UserManageService userManageService;

    @Operation(summary = "사용자 전체 조회 (탈퇴 사용자 제외)", parameters = {
            @Parameter(name = "pageable", description = "페이지, size=10, sort=id,desc 적용 중"),
    })
    @GetMapping(value = {"/", "/{status}"})
    public ResponseEntity<Page<UserManageDto>> searchAllUsersExceptQuit(
            @PageableDefault(size = 10, sort = "id",  direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable(required = false) ActiveStatus status) {
        return ResponseEntity.ok(userManageService.searchAllUsersExceptQuit(pageable, status));
    }

    @Operation(summary = "회원 정보 수정(닉네임, 이메일)")
    @PutMapping("/update/{id}/info")
    public ResponseEntity<UserManageDto> updateUserInfo(@PathVariable Long id, @RequestBody @Valid UserInfoChangeRequestDto dto) {
        return ResponseEntity.ok(userManageService.updateUserInfo(id, dto));
    }

    @Operation(summary = "회원 임시 비밀번호 발급")
    @PutMapping("/update/{id}/password")
    public ResponseEntity<Void> updateUserPassword(@PathVariable Long id) {
        userManageService.updateUserPassword(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "관리자에 의한 사용자 계정 정지")
    @PutMapping("/suspend/{id}/{type}")
    public ResponseEntity<Void> suspendUser(@PathVariable Long id, @PathVariable ActiveStatus type) {
        userManageService.suspendUser(id, type);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "정지 상태인 계정 정지 해제")
    @PutMapping("/unsuspend/{id}")
    public ResponseEntity<Void> unSuspendUser(@PathVariable Long id) {
        userManageService.unSuspendUser(id);
        return ResponseEntity.ok().build();
    }
}
