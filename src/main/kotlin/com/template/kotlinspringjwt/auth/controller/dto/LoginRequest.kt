package com.template.kotlinspringjwt.auth.controller.dto

import jakarta.validation.constraints.NotNull

data class LoginRequest(
    @field:NotNull val id : String,
    @field:NotNull val password : String
)