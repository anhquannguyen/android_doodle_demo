package com.example.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.doOnLayout


class DoodleLayout : FrameLayout {

    private val doodleView = DoodleView(context)

    constructor(context: Context) : super(context)

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs)

    init {
        addView(doodleView)
        setDrawMode(false)
    }

    fun setDrawMode(draw: Boolean) {
        doodleView.drawMode = draw
    }

    fun getDrawMode(): Boolean = doodleView.drawMode

    fun undo() {
        doodleView.undo()
    }

    fun redo() {
        doodleView.redo()
    }

    fun clearDraw() {
        doodleView.clearDraw()
    }

}