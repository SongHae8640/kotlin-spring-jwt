package com.template.kotlinspringjwt.auth.service

import com.template.kotlinspringjwt.auth.controller.dto.LoginRequest
import com.template.kotlinspringjwt.auth.controller.dto.LoginResponse
import com.template.kotlinspringjwt.member.repository.MemberRepository
import com.template.kotlinspringjwt.security.domain.MemberDetails
import com.template.kotlinspringjwt.security.service.JwtProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val jwtProvider: JwtProvider,
    private val memberRepository: MemberRepository
) {
    fun login(request: LoginRequest) : LoginResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.id, request.password)
        )
        return LoginResponse(
            accessToken = jwtProvider.createAccessToken(authentication.principal as MemberDetails),
            refreshToken = jwtProvider.createRefreshToken(authentication.principal as MemberDetails)
        )
    }

    @Transactional
    fun logout(memberDetails: MemberDetails) {
        memberRepository.findByLoginId(memberDetails.username)
            .orElseThrow { throw IllegalArgumentException("해당하는 사용자를 찾을 수 없습니다.") }
            .updateTokenVersion(memberDetails.getTokenVersion())
    }
}