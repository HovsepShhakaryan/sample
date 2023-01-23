package com.vylo.common.data.endpoint

const val AUTH: String = "auth"
const val JWT: String = "jwt"
const val REFRESH: String = "refresh"
const val SLASH: String = "/"

//End points
const val REFRESH_TOKEN_CALL: String = "$AUTH$SLASH$JWT$SLASH$REFRESH$SLASH"

