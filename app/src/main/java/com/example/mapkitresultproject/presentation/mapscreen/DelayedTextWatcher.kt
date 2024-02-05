package com.example.mapkitresultproject.presentation.mapscreen

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log

class DelayedTextWatcher(private val onTextChangedListener: (String) -> Unit) : TextWatcher {

    private val handler = Handler(Looper.getMainLooper())
    private var inputText = ""

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Не используется в данном примере
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Сохраняем введенный текст
        inputText = s.toString()
        Log.d("MyLog","inputText: $inputText")
        // Удаляем предыдущие вызовы Runnable, чтобы избежать лишних запросов
        handler.removeCallbacksAndMessages(null)
    }

    override fun afterTextChanged(s: Editable?) {
            // Запускаем Runnable с задержкой 500 мс (0.5 сек)
            handler.postDelayed({
                // Вызываем колбэк с отформатированным текстом
                onTextChangedListener.invoke(inputText)
            }, 2000L)

    }
}