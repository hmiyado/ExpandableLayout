package com.github.hmiyado.expandablelayout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.Scroller

/**
 * Created by hmiyado on 2018/04/12.
 *
 * 開いたり閉じたりできるレイアウト
 */
class ExpandableLayout @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    private val shrinkWidthRatio: Float
    private val shrinkHeightRatio: Float
    private val scroller: Scroller
    var state: State = State.Expand
        private set

    init {
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.ExpandableLayout)
        shrinkWidthRatio = attrs.getFloat(R.styleable.ExpandableLayout_exp_shrink_width, 1f)
        shrinkHeightRatio = attrs.getFloat(R.styleable.ExpandableLayout_exp_shrink_height, 1f)
        attrs.recycle()

        scroller = Scroller(context, AccelerateDecelerateInterpolator())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMeasure = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMeasure = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        Log.d("onMeasure", "scroller ${scroller.isFinished} ${scroller.currX} ${scroller.currY}")
        if (scroller.computeScrollOffset()) {
            super.onMeasure(
                    MeasureSpec.makeMeasureSpec(scroller.currX, widthMeasure)
                    , MeasureSpec.makeMeasureSpec(scroller.currY, heightMeasure))
            return
        }

        when (state) {
            ExpandableLayout.State.Expand -> {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            }
            ExpandableLayout.State.Shrink -> super.onMeasure(
                    MeasureSpec.makeMeasureSpec(
                            (widthSize * shrinkWidthRatio).toInt(),
                            widthMeasure
                    ),
                    MeasureSpec.makeMeasureSpec(
                            (heightSize * shrinkHeightRatio).toInt(),
                            heightMeasure
                    )
            )
        }
    }

    fun expand() {
        state = State.Expand
        scroller.forceFinished(true)
        scroller.startScroll(measuredWidth, measuredHeight, (measuredWidth * (1 - shrinkWidthRatio)).toInt(), (measuredHeight * (1 - shrinkHeightRatio)).toInt())
        scroll()
        requestLayout()
    }

    private fun scroll() {
        handler.postDelayed({
            if (!scroller.isFinished) {
                requestLayout()
                scroll()
            }
        }, 10)
    }

    fun shrink() {
        state = State.Shrink
        scroller.forceFinished(true)
        scroller.startScroll(measuredWidth, measuredHeight, -(measuredWidth * (1 - shrinkWidthRatio)).toInt(), -(measuredHeight * (1 - shrinkHeightRatio)).toInt())
        scroll()
        requestLayout()
    }

    fun toggle() {
        when (state) {
            ExpandableLayout.State.Expand -> shrink()
            ExpandableLayout.State.Shrink -> expand()
        }
    }

    enum class State {
        Expand, Shrink
    }
}