package de.leonsuv.stundenplan.wrapper

import java.util.Base64

class UserData (private var username: String,
                private var password: String) {

    private var accessToken = ""
    private var refreshToken = ""

    fun setUsername(username: String) {
        this.username = username
    }

    fun getUsername(): String {
        return this.username
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun getBase64(): String {
        //TODO: "username:password" to Base64
        return Base64.getEncoder().encodeToString(("$username:$password").toByteArray())
    }

    fun setAccessToken(accessToken: String) {
        this.accessToken = accessToken
    }

    fun getAccessToken(): String {
        return accessToken
    }

    fun setRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }

    fun getRefreshToken(): String {
        return refreshToken
    }

    fun isAuthorized(): Boolean {
        return getAccessToken() != "" && getRefreshToken() != ""
    }

    override fun toString(): String {
        return ("Username: " + getUsername() + "\n"
                + "getBase64: " + getBase64() + "\n"
                + "AccessToken: " + getAccessToken() + "\n"
                + "RefreshToken: " + getRefreshToken())
    }
}