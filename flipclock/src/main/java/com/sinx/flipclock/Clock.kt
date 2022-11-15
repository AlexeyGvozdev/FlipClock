package com.sinx.flipclock

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import kotlin.math.max
import kotlin.math.min

class Clock @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val numberHourView = Number(context, attrs, defStyleAttr)
    private val numberMinuteView = Number(context, attrs, defStyleAttr)

    private val hour = "10"
    private val min = "45"

    init {
        numberHourView.text = hour
        numberMinuteView.text = min
        addView(numberHourView)
        addView(numberMinuteView)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val measureWidth = measureDimension(desiredWidth, widthMeasureSpec)
        val measureHeight = measureDimension(desiredHeight, heightMeasureSpec)
        val numberWidth = min(measureWidth / 2, measureHeight)
        numberHourView.measure(
            MeasureSpec.makeMeasureSpec(numberWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(numberWidth, MeasureSpec.EXACTLY),
        )
        numberMinuteView.measure(
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
        numberHourView.layout(
            (r - l) / 2 - numberHourView.measuredWidth,
            0,
            (r - l) / 2,
            b - t
        )
        numberMinuteView.layout(
            (r - l) / 2,
            0,
            (r - l) / 2 + numberMinuteView.measuredWidth,
            b - t
        )
    }
}