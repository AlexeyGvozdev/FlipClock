package com.sinx.flipclock

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import java.util.Calendar
import kotlin.math.max
import kotlin.math.min

class Clock @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val startNumber = Number(context, attrs, defStyleAttr)
    private val endNumber = Number(context, attrs, defStyleAttr)

    private val chronometer = Chronometer(TimePeriod.SECONDS) { start, end ->
        startNumber.text = start.coerce()
        endNumber.text = end.coerce()
    }
    private val startValue = Calendar.getInstance().get(Calendar.MINUTE).toString()
    private val endValue = Calendar.getInstance().get(Calendar.SECOND).toString()

    init {
        startNumber.text = startValue
        endNumber.text = endValue
        addView(startNumber)
        addView(endNumber)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        chronometer.cancel()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        chronometer.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val measureWidth = measureDimension(desiredWidth, widthMeasureSpec)
        val measureHeight = measureDimension(desiredHeight, heightMeasureSpec)
        val numberWidth = min(measureWidth / 2, measureHeight)
        startNumber.measure(
            MeasureSpec.makeMeasureSpec(numberWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(numberWidth, MeasureSpec.EXACTLY),
        )
        endNumber.measure(
            MeasureSpec.makeMeasureSpec(numberWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(numberWidth, MeasureSpec.EXACTLY),
        )
        setMeasuredDimension(measureWidth, measureHeight)
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
                result = max(result, specSize)
            }
        }
        if (result < desiredSize) {
            Log.e("FlipClock", "The view is too small, the content might get cut")
        }
        return result
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        startNumber.layout(
            (r - l) / 2 - startNumber.measuredWidth,
            0,
            (r - l) / 2,
            b - t
        )
        endNumber.layout(
            (r - l) / 2,
            0,
            (r - l) / 2 + endNumber.measuredWidth,
            b - t
        )
    }
}