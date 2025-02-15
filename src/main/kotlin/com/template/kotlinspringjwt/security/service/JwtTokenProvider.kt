package com.template.kotlinspringjwt.security.service

import com.template.kotlinspringjwt.security.domain.MemberDetails
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

//TODO : 이름 변경. JwtTokenProvider -> JwtProvider, ttl -> milliseconds
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
            .setExpiration(Date(Date().time + accessTokenTtl))
            .compact()

    }

    fun createRefreshToken(memberDetails: MemberDetails): String {
        return Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray()), SignatureAlgorithm.HS512)
            .setIssuedAt(Date())
            .setSubject(memberDetails.username)
            .claim("version", memberDetails.getTokenVersion())
            .setExpiration(Date(Date().time + refreshTokenTtl))
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.toByteArray()))
                .build()
                .parseClaimsJws(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getAuthentication(token: String): Authentication {
        val memberDetails = getMemberDetails(token)
        return UsernamePasswordAuthenticationToken(memberDetails, "", memberDetails.authorities)
    }

    private fun getMemberDetails(token: String): MemberDetails {
        val claims = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.toByteArray()))
            .build()
            .parseClaimsJws(token)
            .body

        return MemberDetails(
            seq = (claims["seq"] as Int).toLong(),
            loginId = claims.subject,
            name = claims["name"] as String
        )
    }
}