package com.template.kotlinspringjwt.config

import com.template.kotlinspringjwt.security.filter.JwtAuthenticationFilter
import com.template.kotlinspringjwt.security.service.JwtProvider
import com.template.kotlinspringjwt.security.service.MemberDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val memberDetailsService: MemberDetailsService,
    private val jwtProvider: JwtProvider
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity) : SecurityFilterChain {
        return http
            .authorizeHttpRequests{ it.anyRequest().permitAll()}
            .csrf{ it.disable() }
            .formLogin{ it.disable() }
            .httpBasic{ it.disable() }
            .addFilterBefore(JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun authenticationManager () : AuthenticationManager {
        return ProviderManager(listOf(memberAuthenticationProvider()))
    }

    @Bean
    fun memberAuthenticationProvider() : AuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(memberDetailsService)
        provider.setPasswordEncoder(passwordEncoder())
        return provider
    }

    @Bean
    fun passwordEncoder() : PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}