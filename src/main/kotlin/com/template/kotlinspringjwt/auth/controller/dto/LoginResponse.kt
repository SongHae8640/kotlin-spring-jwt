package com.template.kotlinspringjwt.auth.controller.dto

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)