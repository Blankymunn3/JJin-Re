package com.jjin_re.utils

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.ref.WeakReference

class EditTextDebounce private constructor(editText: EditText) {
    private val editTextWeakReference: WeakReference<EditText>?
    private val debounceHandler: Handler = Handler(Looper.getMainLooper())
    private var debounceCallback: DebounceCallback? = null
    private var debounceWorker: Runnable
    private var delayMillis: Int
    private val textWatcher: TextWatcher
    fun watch(debounceCallback: DebounceCallback?) {
        this.debounceCallback = debounceCallback
    }

    fun watch(debounceCallback: DebounceCallback?, delayMillis: Int) {
        this.debounceCallback = debounceCallback
        this.delayMillis = delayMillis
    }

    fun unwatch() {
        if (editTextWeakReference != null) {
            val editText = editTextWeakReference.get()
            if (editText != null) {
                editText.removeTextChangedListener(textWatcher)
                editTextWeakReference.clear()
                debounceHandler.removeCallbacks(debounceWorker)
            }
        }
    }

    private fun setDelayMillis(delayMillis: Int) {
        this.delayMillis = delayMillis
    }

    private class DebounceRunnable internal constructor(
        private val result: String,
        private val debounceCallback: DebounceCallback?
    ) :
        Runnable {
        override fun run() {
            debounceCallback?.onFinished(result)
        }

    }

    interface DebounceCallback {
        fun onFinished(result: String)
    }

    companion object {
        fun create(editText: EditText): EditTextDebounce {
            return EditTextDebounce(editText)
        }

        fun create(editText: EditText, delayMillis: Int): EditTextDebounce {
            val editTextDebounce = EditTextDebounce(editText)
            editTextDebounce.setDelayMillis(delayMillis)
            return editTextDebounce
        }
    }

    init {
        debounceWorker = DebounceRunnable("", null)
        delayMillis = 300
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
                //unused
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                //unused
            }

            override fun afterTextChanged(s: Editable) {
                debounceHandler.removeCallbacks(debounceWorker)
                debounceWorker = DebounceRunnable(s.toString(), debounceCallback)
                debounceHandler.postDelayed(debounceWorker, delayMillis.toLong())
            }
        }
        editTextWeakReference = WeakReference(editText)
        val editTextInternal = editTextWeakReference.get()
        editTextInternal?.addTextChangedListener(textWatcher)
    }
}