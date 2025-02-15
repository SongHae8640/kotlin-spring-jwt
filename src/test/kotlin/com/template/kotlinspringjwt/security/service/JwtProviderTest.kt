package com.template.kotlinspringjwt.security.service

import com.template.kotlinspringjwt.member.domain.Member
import com.template.kotlinspringjwt.security.domain.MemberDetails
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JwtProviderTest{
    @Autowired
    lateinit var jwtProvider: JwtProvider
    @Test
    fun createAccessTokenTest(){
        // Given
        val member = Member(1, "string", "test", "조조",1)
        val memberDetails = MemberDetails(member)

        // When
        val token = jwtProvider.createAccessToken(memberDetails)

        // Then
        assertNotNull(token)
        println(token)
    }

    @Test
    fun createRefreshTokenTest(){
        // Given
        val member = Member(1, "string", "test", "조조", 1)
        val memberDetails = MemberDetails(member)

        // When
        val token = jwtProvider.createRefreshToken(memberDetails)

        // Then
        assertNotNull(token)
        println(token)
    }
}