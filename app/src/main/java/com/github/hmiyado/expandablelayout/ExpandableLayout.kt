package com.github.hmiyado.expandablelayout

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

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
    var state: State = State.Expand
        private set

    init {
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.ExpandableLayout)
        shrinkWidthRatio = attrs.getFloat(R.styleable.ExpandableLayout_exp_shrink_width, 1f)
        shrinkHeightRatio = attrs.getFloat(R.styleable.ExpandableLayout_exp_shrink_height, 1f)
        attrs.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMeasure = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMeasure = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        when (state) {
            ExpandableLayout.State.Expand -> super.onMeasure(widthMeasureSpec, heightMeasureSpec)
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
        requestLayout()
    }

    fun shrink() {
        state = State.Shrink
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