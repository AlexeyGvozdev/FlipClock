package com.sinx.flipclock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

class FlipClock @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val textPaint = TextPaint().apply {
        color = Color.BLACK
    }

    private val text = "88"
    private val bounds = Rect()
    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            textPaint.textSize = calculateMaxTextSize(maxWidth = width)
            val xPos = 0
            textPaint.getTextBounds(text, 0, text.length, bounds)
            val yPos = height / 2 + bounds.height() / 2
            it.drawText(text, xPos.toFloat(), yPos.toFloat(), textPaint)
        }
    }

    private fun calculateMaxTextSize(
        text: String = this.text,
        paint: Paint = textPaint,
        maxWidth: Int
    ): Float {
        var size = 1.0f
        val step = 1.0f
        while (true) {
            if (paint.measureText(text) < maxWidth) {
                size += step
                paint.textSize = size
            } else {
                return size - step
            }
        }
    }
}
