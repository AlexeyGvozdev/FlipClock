package com.sinx.flipclock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.min

class Divider @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val textPaint = TextPaint().apply {
        color = Color.BLACK
    }

    var text = ":"
    private val bounds = Rect()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val measureWidth = measureDimension(desiredWidth, widthMeasureSpec)
        val measureHeight = measureDimension(desiredHeight, heightMeasureSpec)
        textPaint.textSize =
            minOf(measureHeight, measureWidth) / textPaint.measureText(text) * textPaint.textSize
        setMeasuredDimension(
            measureWidth,
            measureHeight
        )
    }

    private fun measureDimension(desiredSize: Int, measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        var result: Int
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = desiredSize
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
        }
        if (result < desiredSize) {
            Log.e("FlipClock", "The view is too small, the content might get cut")
        }
        return result
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            textPaint.getTextBounds(text, 0, text.length, bounds)
            val yPos = height.half + bounds.height().half
            it.drawText(text, 0f, yPos.toFloat(), textPaint)
        }
    }

    private val Int.half
        get() = this / 2

}