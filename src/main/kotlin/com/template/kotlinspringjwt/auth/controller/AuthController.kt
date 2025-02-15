package com.template.kotlinspringjwt.auth.controller

import com.template.kotlinspringjwt.auth.controller.dto.LoginRequest
import com.template.kotlinspringjwt.auth.controller.dto.LoginResponse
import com.template.kotlinspringjwt.auth.controller.dto.ValidateResponse
import com.template.kotlinspringjwt.auth.service.AuthService
import com.template.kotlinspringjwt.security.domain.MemberDetails
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 API")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "회원의 AccessToken, RefreshToken 발급")
    fun login(@RequestBody @Validated request: LoginRequest) : LoginResponse {
        return authService.login(request)
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "회원의 JWT 무효화")
    fun logout(@AuthenticationPrincipal memberDetails: MemberDetails) {
        authService.logout(memberDetails)
    }

    @GetMapping("/validate")
    @Operation(summary = "토큰 검증", description = "토큰의 유효성 검증")
    fun validate(@AuthenticationPrincipal memberDetails: MemberDetails) : ValidateResponse {
        return ValidateResponse(memberDetails)
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급", description = "RefreshToken을 이용한 AccessToken 재발급")
    fun refresh() {
        // TODO : 토큰 재발급 로직
    }
}