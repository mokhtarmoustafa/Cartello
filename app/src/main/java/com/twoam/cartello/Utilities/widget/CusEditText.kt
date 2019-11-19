package com.twoam.cartello.Utilities.widget

import android.content.Context
import android.os.SystemClock
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper
import android.widget.EditText
import android.widget.TextView

import com.twoam.cartello.R


class CusEditText(context: Context, attrs: AttributeSet) : EditText(context, attrs) {

    private val mDefinedActionId: Int
    private var mLastEditorActionTime = 0L

    init {

        // Corresponds to 'android:imeActionId' value
        mDefinedActionId = resources.getInteger(R.integer.definedActionId)

        setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            Log.i("CusEditText", "onEditorAction, actionId = $actionId")

            // Only bother if (...)
            if (actionId == mDefinedActionId) {

                // setInputType(...) will restart the IME
                // and call finishComposingText()
                // see below
                mLastEditorActionTime = SystemClock.elapsedRealtime()

                // Check if current InputType is NUMBER
                if (inputType and InputType.TYPE_CLASS_NUMBER != 0) {
                    // Toggle
                    setImeActionLabel("NUM", mDefinedActionId)
                    inputType = InputType.TYPE_CLASS_TEXT
                } else {
                    // Current InputType is TEXT // Toggle
                    setImeActionLabel("ABC", mDefinedActionId)
                    inputType = InputType.TYPE_CLASS_NUMBER
                }

                // We've handled this
                return@OnEditorActionListener true
            }

            // Let someone else worry about this
            false
        })
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
        val inputConnection = super.onCreateInputConnection(outAttrs)

        return CusInputConnectionWrapper(inputConnection, false)
    }

    private inner class CusInputConnectionWrapper internal constructor(target: InputConnection, mutable: Boolean) : InputConnectionWrapper(target, mutable) {

        override fun finishComposingText(): Boolean {
            Log.i("CICW", "finishComposingText")

            // Ignore finishComposingText for 1 second (1000L)
            if (SystemClock.elapsedRealtime() - mLastEditorActionTime > 1000L) {
                if (inputType and InputType.TYPE_CLASS_NUMBER == 0) {
                    // InputConnection is no longer valid.
                    // Switch back to NUMBER iff required
                    setImeActionLabel("ABC", mDefinedActionId)
                    inputType = InputType.TYPE_CLASS_NUMBER
                }
            }

            return super.finishComposingText()
        }
    }
}
