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

internal class Number @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val textPaint = TextPaint().apply {
        color = Color.BLACK
    }

    var text = "88"
        set(value) {
            field = value
            invalidate()
        }
    private var containerForNumber = Rect()
    private val dividerHeight = 5
    private val rect = Rect()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val measureWidth = measureDimension(desiredWidth, widthMeasureSpec)
        val measureHeight = measureDimension(desiredHeight, heightMeasureSpec)
        textPaint.textSize =
            minOf(measureHeight, measureWidth) / textPaint.measureText(text) * textPaint.textSize
        if (containerForNumber.isEmpty) {
            (0..9).forEach {
                textPaint.getTextBounds("$it$it", 0, 2, rect)
                if (rect.height() > containerForNumber.height()) {
                    containerForNumber = rect
                }
            }
        }
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
            textPaint.color = Color.BLACK
            val yPos = height.half + containerForNumber.height().half
            it.save()
            it.clipRect(
                0,
                0,
                width,
                height.half - dividerHeight
            )
            it.drawText(text, 0f, yPos.toFloat(), textPaint)
            it.restore()
            it.clipRect(
                0,
                height.half + dividerHeight,
                width,
                height
            )
            textPaint.color = Color.BLUE
            it.drawText(text, 0f, yPos.toFloat(), textPaint)
        }
    }
}
