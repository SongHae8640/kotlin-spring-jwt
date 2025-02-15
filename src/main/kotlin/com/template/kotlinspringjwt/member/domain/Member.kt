package com.template.kotlinspringjwt.member.domain

import jakarta.persistence.*


@Entity
@Table(name = "tb_member")
class Member (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val seq: Long? = null,

    @Column(nullable = false, unique = true, length = 50)
    private val loginId: String = "",

    @Column(nullable = false, length = 100)
    private val password: String = "",

    @Column(nullable = false, length = 50)
    private val name: String = "",

    @Column(nullable = false, length = 50)
    private var tokenVersion: Long = 0
){
    fun getSeq(): Long? = seq
    fun getLoginId(): String = loginId
    fun getPassword(): String = password
    fun getName(): String = name
    fun getTokenVersion(): Long = tokenVersion

    fun updateTokenVersion(tokenVersion: Long) {
        if (this.tokenVersion != tokenVersion) {
            throw IllegalArgumentException("토큰 버전이 일치하지 않습니다.")
        }
        this.tokenVersion++
    }
}