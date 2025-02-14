package com.template.kotlinspringjwt.security.service

import com.template.kotlinspringjwt.security.domain.MemberDetails
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.ttl.access}") private val accessTokenTtl: Long,
    @Value("\${jwt.ttl.refresh}") private val refreshTokenTtl: Long
) {
    fun createAccessToken(memberDetails: MemberDetails): String {
        return Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray()), SignatureAlgorithm.HS512)
            .setIssuedAt(Date())
            .setSubject(memberDetails.username)
            .claim("seq", memberDetails.getSeq())
            .claim("name", memberDetails.getName())
            .setExpiration(Date(System.currentTimeMillis() + accessTokenTtl))
            .compact()

    }

    fun createRefreshToken(memberDetails: MemberDetails): String {
        return Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray()), SignatureAlgorithm.HS512)
            .setIssuedAt(Date())
            .setSubject(memberDetails.username)
            .setExpiration(Date(System.currentTimeMillis() + refreshTokenTtl))
            .compact()
    }
}