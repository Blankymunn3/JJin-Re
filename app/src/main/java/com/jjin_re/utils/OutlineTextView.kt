package com.jjin_re.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.jjin_re.R


class OutlineTextView : AppCompatTextView {
    private var stroke = false
    private var strokeWidth = 0.0f
    private var strokeColor = 0

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    @SuppressLint("Recycle")
    private fun initView(context: Context, attrs: AttributeSet?) {
        val type: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.OutlineTextView)
        stroke = type.getBoolean(R.styleable.OutlineTextView_textStroke, false) // 외곽선 유무
        strokeWidth = type.getFloat(R.styleable.OutlineTextView_textStrokeWidth, 0.0f) // 외곽선 두께
        strokeColor = type.getColor(R.styleable.OutlineTextView_textStrokeColor, -0x1) // 외곽선
    }

    override fun onDraw(canvas: Canvas?) {
        if (stroke) {
            val states = textColors
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = strokeWidth
            setTextColor(strokeColor)
            super.onDraw(canvas)
            paint.style = Paint.Style.FILL
            setTextColor(states)
        }
        super.onDraw(canvas)
    }
}