package com.example.mystoryapp.ui.view

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.mystoryapp.R

class EditTextPassword : AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        setHint(R.string.hint_password)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setAutofillHints(AUTOFILL_HINT_PASSWORD)
        }

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val password = s.toString()

                when {
                    password.isEmpty() -> error = context.getString(R.string.error_empty_password)
                    password.length < 8 -> error = context.getString(R.string.error_password_too_short)
                    else -> error = null
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        transformationMethod = PasswordTransformationMethod.getInstance()
    }
}
