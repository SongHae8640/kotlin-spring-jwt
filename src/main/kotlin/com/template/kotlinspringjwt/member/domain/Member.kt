package com.template.kotlinspringjwt.member.domain

import jakarta.persistence.*


@Entity
@Table(name = "tb_member")
class Member (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val seq: Long? = null,

    @Column(nullable = false, unique = true, length = 50)
    val loginId: String,

    @Column(nullable = false, length = 100)
    val password: String,

    @Column(nullable = false, length = 50)
    val name: String,

    @Column(nullable = false, length = 50)
    var tokenVersion: Long
){
    constructor() : this(null, "","","", 1)
}