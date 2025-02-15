package com.template.kotlinspringjwt.security.service

import com.template.kotlinspringjwt.member.repository.MemberRepository
import com.template.kotlinspringjwt.security.domain.MemberDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberDetailsService(
    private val memberRepository: MemberRepository
): UserDetailsService {

    @Transactional
    override fun loadUserByUsername(loginId: String): UserDetails {
        val member = memberRepository.findByLoginId(loginId)
            .orElseThrow { throw IllegalArgumentException("해당하는 사용자를 찾을 수 없습니다.") }
        member.updateTokenVersion(member.getTokenVersion())
        return MemberDetails(member)
    }
}