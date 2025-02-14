package com.template.kotlinspringjwt.member.repository

import com.template.kotlinspringjwt.member.domain.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface MemberRepository : JpaRepository<Member, Long> {
    fun findByLoginId(loginId: String): Optional<Member>
}