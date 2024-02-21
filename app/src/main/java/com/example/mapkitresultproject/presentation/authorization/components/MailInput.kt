package com.example.mapkitresultproject.presentation.authorization.components

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import com.example.mapkitresultproject.R

class MailInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : CustomInputLayout(context, attrs, defStyleAttr) {
    override val errorMessageId = R.string.login_error

    override fun innerIsValid(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(text()).matches()
    }
}