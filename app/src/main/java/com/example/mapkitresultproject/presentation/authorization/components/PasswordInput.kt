package com.example.mapkitresultproject.presentation.authorization.components

import android.content.Context
import android.util.AttributeSet
import com.example.mapkitresultproject.R

class PasswordInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : CustomInputLayout(context, attrs, defStyleAttr) {

    override val errorMessageId: Int = R.string.password_error

    override fun innerIsValid(): Boolean {
        return text().matches(Regex(PASSWORD_PATTERN))
    }

    companion object {

        private const val PASSWORD_PATTERN =
            "^(?=.*?[A-Z]).{2,}\$"
            //"^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$"
    }
}