package com.lee.remember.android.utils

fun getErrorMessage(errorCode: String?) = run {
    when (errorCode) {
        "DUPLICATED_EMAIL" -> "중복된 이메일입니다."
        "USER_NOT_FOUND" -> "없는 사용자입니다."
        "INVALID_PASSWORD" -> "비밀번호를 확인해주세요."
        else -> "고객센터로 문의해주세요."
    }
}