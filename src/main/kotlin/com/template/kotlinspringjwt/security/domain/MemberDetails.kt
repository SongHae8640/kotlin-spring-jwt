package com.template.kotlinspringjwt.security.domain

import com.template.kotlinspringjwt.member.domain.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class MemberDetails(private val member: Member) : UserDetails {
    fun getSeq(): Long = member.seq!!
    fun getName(): String = member.name
    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(SimpleGrantedAuthority("ROLE_USER"))
    override fun getPassword(): String = member.password
    override fun getUsername(): String = member.loginId
}