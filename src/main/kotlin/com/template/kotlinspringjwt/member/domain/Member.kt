package com.template.kotlinspringjwt.member.domain

import jakarta.persistence.*


@Entity
@Table(name = "tb_member")
class Member (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val seq: Long? = null,

    @Column(nullable = false, unique = true, length = 50)
    private val loginId: String,

    @Column(nullable = false, length = 100)
    private val password: String,

    @Column(nullable = false, length = 50)
    private val name: String,

    @Column(nullable = false, length = 50)
    private var tokenVersion: Long
){
    constructor() : this(null, "","","", 1)

    fun getSeq(): Long? = seq
    fun getLoginId(): String = loginId
    fun getPassword(): String = password
    fun getName(): String = name
    fun getTokenVersion(): Long = tokenVersion
}