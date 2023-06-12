package com.dtalks.dtalks.user.Util;

import com.dtalks.dtalks.exception.exception.CustomException;
import com.dtalks.dtalks.exception.ErrorCode;
import com.dtalks.dtalks.user.entity.User;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor
public class SecurityUtil {

    // Request 가 들어올 때 JwtFilter 의 doFilter 에서 저장
    public static User getUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }
}
