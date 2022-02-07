package pk.utils.lib

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Author: pk
 * Time: 2021/9/14  11:26 上午
 * Description:
 */
class CardLayoutManager(
    private var mRecyclerView: RecyclerView,
    private var mItemTouchHelper: ItemTouchHelper?
) : RecyclerView.LayoutManager() {

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams =
        RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        detachAndScrapAttachedViews(recycler!!)
        // 当数据源个数大于最大显示数时
        if (itemCount > CardConfig.DEFAULT_SHOW_ITEM) {
            for (pos in CardConfig.DEFAULT_SHOW_ITEM downTo 0) {
                val view = recycler.getViewForPosition(pos)
                addView(view)
                measureChildWithMargins(view, 0, 0)
                val widthSpace = width - getDecoratedMeasuredWidth(view)
                val heightSpace = height - getDecoratedMeasuredHeight(view)
                layoutDecoratedWithMargins(
                    view,
                    widthSpace / 2,
                    heightSpace / 2,
                    widthSpace / 2 + getDecoratedMeasuredWidth(view),
                    heightSpace / 2 + getDecoratedMeasuredHeight(view)
                )
                when {
                    pos == CardConfig.DEFAULT_SHOW_ITEM -> {
                        view.scaleX = (1 - (pos - 1) * CardConfig.DEFAULT_SCALE)
                        view.scaleY = (1 - (pos - 1) * CardConfig.DEFAULT_SCALE)
                        view.translationY =
                            ((pos - 1) * view.measuredHeight / CardConfig.DEFAULT_TRANSLATE_Y).toFloat()
                    }
                    pos > 0 -> {
                        view.scaleX = 1 - pos * CardConfig.DEFAULT_SCALE
                        view.scaleY = 1 - pos * CardConfig.DEFAULT_SCALE
                        view.translationY =
                            (pos * view.measuredHeight / CardConfig.DEFAULT_TRANSLATE_Y).toFloat()
                    }
                    else -> {
                        view.setOnTouchListener(mOnTouchListener)
                    }
                }
            }
        }else{
            // 当数据源个数小于或等于最大显示数时
            for (position in itemCount - 1 downTo 0) {
                val view = recycler.getViewForPosition(position)
                addView(view)
                measureChildWithMargins(view, 0, 0)
                val widthSpace = width - getDecoratedMeasuredWidth(view)
                val heightSpace = height - getDecoratedMeasuredHeight(view)
                // recyclerview 布局
                layoutDecoratedWithMargins(
                    view, widthSpace / 2, heightSpace / 2,
                    widthSpace / 2 + getDecoratedMeasuredWidth(view),
                    heightSpace / 2 + getDecoratedMeasuredHeight(view)
                )
                if (position > 0) {
                    view.scaleX = 1 - position * CardConfig.DEFAULT_SCALE
                    view.scaleY = 1 - position * CardConfig.DEFAULT_SCALE
                    view.translationY =
                        (position * view.measuredHeight / CardConfig.DEFAULT_TRANSLATE_Y).toFloat()
                } else {
                    view.setOnTouchListener(mOnTouchListener)
                }
            }
        }
    }

    private val mOnTouchListener = View.OnTouchListener { view, event ->
        val childViewHolder = view?.let { mRecyclerView.getChildViewHolder(it) }
        if (event?.action == MotionEvent.ACTION_DOWN) {
            childViewHolder?.let { mItemTouchHelper?.startSwipe(it) }
        }
        false
    }

}