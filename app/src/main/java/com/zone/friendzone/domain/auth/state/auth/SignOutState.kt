package com.zone.friendzone.domain.auth.state.auth

data class SignOutState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""

)