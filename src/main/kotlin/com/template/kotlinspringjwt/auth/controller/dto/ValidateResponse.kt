package com.template.kotlinspringjwt.auth.controller.dto

import com.template.kotlinspringjwt.security.domain.MemberDetails

data class ValidateResponse(
    val seq: Long,
    val id: String,
    val name: String
) {
    constructor(memberDetails: MemberDetails) : this(
        seq = memberDetails.getSeq(),
        id = memberDetails.getUsername(),
        name = memberDetails.getName()
    )
}