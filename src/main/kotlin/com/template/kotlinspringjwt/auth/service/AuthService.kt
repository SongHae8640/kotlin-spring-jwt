package com.template.kotlinspringjwt.auth.service

import com.template.kotlinspringjwt.auth.controller.dto.LoginRequest
import com.template.kotlinspringjwt.auth.controller.dto.LoginResponse
import com.template.kotlinspringjwt.security.domain.MemberDetails
import com.template.kotlinspringjwt.security.service.JwtTokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun login(request: LoginRequest) : LoginResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.id, request.password)
        )
        return LoginResponse(
            accessToken = jwtTokenProvider.createAccessToken(authentication.principal as MemberDetails),
            refreshToken = jwtTokenProvider.createRefreshToken(authentication.principal as MemberDetails)
        )
    }
}