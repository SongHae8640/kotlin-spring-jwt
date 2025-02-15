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
        return createLoginResponse(authentication.principal as MemberDetails)
    }

    private fun createLoginResponse(memberDetails: MemberDetails) =
        LoginResponse(
            accessToken = jwtProvider.createAccessToken(memberDetails),
            refreshToken = jwtProvider.createRefreshToken(memberDetails)
        )

    @Transactional
    fun logout(memberDetails: MemberDetails) {
        memberRepository.findByLoginId(memberDetails.username)
            .orElseThrow { throw IllegalArgumentException("해당하는 사용자를 찾을 수 없습니다.") }
            .updateTokenVersion(memberDetails.getTokenVersion())
    }

    @Transactional
    fun refresh(memberDetails: MemberDetails, refreshToken: String): LoginResponse {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw IllegalArgumentException("RefreshToken이 유효하지 않습니다.")
        }

        val member = memberRepository.findByLoginId(memberDetails.username)
            .orElseThrow { throw IllegalArgumentException("해당하는 사용자를 찾을 수 없습니다.") }
        member.updateTokenVersion(memberDetails.getTokenVersion())

        return createLoginResponse(MemberDetails(member))
    }
}