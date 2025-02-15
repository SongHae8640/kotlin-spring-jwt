package com.template.kotlinspringjwt.security.domain

import com.template.kotlinspringjwt.member.domain.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class MemberDetails(private val member: Member) : UserDetails {
    fun getSeq(): Long = member.getSeq()!!
    fun getName(): String = member.getName()
    fun getTokenVersion(): Long = member.getTokenVersion()
    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(SimpleGrantedAuthority("ROLE_USER"))
    override fun getPassword(): String = member.getPassword()
    override fun getUsername(): String = member.getLoginId()
}