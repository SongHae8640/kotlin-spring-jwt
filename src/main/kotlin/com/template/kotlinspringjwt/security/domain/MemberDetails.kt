package com.template.kotlinspringjwt.security.domain

import com.template.kotlinspringjwt.member.domain.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class MemberDetails(
    private val seq: Long,
    private val loginId: String,
    private val password: String,
    private val tokenVersion: Long,
    private val name: String
) : UserDetails {

    constructor(member: Member) : this(
        seq = member.getSeq() ?: throw IllegalArgumentException("Member seq is null"),
        loginId = member.getLoginId(),
        password = member.getPassword(),
        tokenVersion = member.getTokenVersion(),
        name = member.getName()
    )

    constructor(seq: Long, loginId: String, name: String) : this(
        seq = seq,
        loginId = loginId,
        password = "",
        tokenVersion = 0,
        name = name
    )

    fun getSeq(): Long = seq
    fun getName(): String = name
    fun getTokenVersion(): Long = tokenVersion

    override fun getAuthorities(): Collection<GrantedAuthority> =
    listOf(SimpleGrantedAuthority("ROLE_USER"))
    override fun getPassword(): String = password
    override fun getUsername(): String = loginId
}