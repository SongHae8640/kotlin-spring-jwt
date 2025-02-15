package com.template.kotlinspringjwt.security.service

import com.template.kotlinspringjwt.security.domain.MemberDetails
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class JwtProvider(
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.lifetime.access}") private val accessTokenLifetime: Duration,
    @Value("\${jwt.lifetime.refresh}") private val refreshTokenLifetime: Duration
) {
    private val accessTokenLifetimeMillis = accessTokenLifetime.toMillis()
    private val refreshTokenLifetimeMillis = refreshTokenLifetime.toMillis()
    private val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun createAccessToken(memberDetails: MemberDetails): String {
        return Jwts.builder()
            .signWith(key, SignatureAlgorithm.HS512)
            .setIssuedAt(Date())
            .setSubject(memberDetails.username)
            .claim("seq", memberDetails.getSeq())
            .claim("name", memberDetails.getName())
            .claim("version", memberDetails.getTokenVersion())
            .setExpiration(Date(Date().time + accessTokenLifetimeMillis))
            .compact()

    }

    fun createRefreshToken(memberDetails: MemberDetails): String {
        return Jwts.builder()
            .signWith(key, SignatureAlgorithm.HS512)
            .setIssuedAt(Date())
            .setSubject(memberDetails.username)
            .claim("version", memberDetails.getTokenVersion())
            .setExpiration(Date(Date().time + refreshTokenLifetimeMillis))
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
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
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

        return MemberDetails(
            seq = (claims["seq"] as Int).toLong(),
            loginId = claims.subject,
            tokenVersion = (claims["version"] as Int).toLong(),
            name = claims["name"] as String
        )
    }
}