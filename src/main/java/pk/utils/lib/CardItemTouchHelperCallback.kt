package pk.utils.lib

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import java.util.function.Consumer

/**
 * Author: pk
 * Time: 2021/9/14  4:46 下午
 * Description:
 */
class CardItemTouchHelperCallback<T> constructor(
    val adapter: Adapter<RecyclerView.ViewHolder>,
    val dataList: MutableList<T>,
    val onSwipeListener: OnSwipeListener<T>?
) :
    ItemTouchHelper.Callback() {


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        var dragFlag = 0
        var swipeFlag = 0
        if (recyclerView.layoutManager is CardLayoutManager) {
            swipeFlag = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        }
        return makeMovementFlags(dragFlag, swipeFlag)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        // 移除 onTouchListener,否则触摸滑动会乱了
        viewHolder.itemView.setOnTouchListener(null)
        val layoutPosition = viewHolder.layoutPosition
        val remove: T = dataList.removeAt(layoutPosition)
        adapter.notifyDataSetChanged()
        onSwipeListener?.onSwiped(
            viewHolder,
            remove,
            if (direction == ItemTouchHelper.LEFT) CardConfig.SWIPED_LEFT else CardConfig.SWIPED_RIGHT
        )
        // 当没有数据时回调 mListener
        if (adapter.itemCount == 0) {
            onSwipeListener?.onSwipedClear()
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.rotation = 0f
    }

    override fun isItemViewSwipeEnabled(): Boolean = false

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            var ratio = dX / getThreshold(recyclerView, viewHolder)
            // ratio 最大为 1 或 -1
            if (ratio > 1) {
                ratio = 1f
            } else if (ratio < -1) {
                ratio = -1f
            }
            itemView.rotation = ratio * CardConfig.DEFAULT_ROTATE_DEGREE
            val childCount = recyclerView.childCount
            // 当数据源个数大于最大显示数时
            if (childCount > CardConfig.DEFAULT_SHOW_ITEM) {
                for (pos in 1 until childCount - 1) {
                    val index = childCount - pos - 1
                    val view = recyclerView.getChildAt(pos)
                    view.scaleX =
                        1 - index * CardConfig.DEFAULT_SCALE + Math.abs(ratio) * CardConfig.DEFAULT_SCALE
                    view.scaleY =
                        1 - index * CardConfig.DEFAULT_SCALE + Math.abs(ratio) * CardConfig.DEFAULT_SCALE
                    view.translationY =
                        (index - Math.abs(ratio)) * itemView.measuredHeight / CardConfig.DEFAULT_TRANSLATE_Y
                }
            } else {
                // 当数据源个数小于或等于最大显示数时
                for (position in 0 until childCount - 1) {
                    val index = childCount - position - 1
                    val view = recyclerView.getChildAt(position)
                    view.scaleX =
                        1 - index * CardConfig.DEFAULT_SCALE + Math.abs(ratio) * CardConfig.DEFAULT_SCALE
                    view.scaleY =
                        1 - index * CardConfig.DEFAULT_SCALE + Math.abs(ratio) * CardConfig.DEFAULT_SCALE
                    view.translationY =
                        (index - Math.abs(ratio)) * itemView.measuredHeight / CardConfig.DEFAULT_TRANSLATE_Y
                }
            }

            if (onSwipeListener != null) {
                if (ratio != 0f) {
                    onSwipeListener.onSwiping(
                        viewHolder,
                        ratio,
                        if (ratio < 0) CardConfig.SWIPING_LEFT else CardConfig.SWIPING_RIGHT
                    )
                } else {
                    onSwipeListener.onSwiping(viewHolder, ratio, CardConfig.SWIPING_NONE)
                }
            }
        }

    }

    private fun getThreshold(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Float {
        return recyclerView.width * getSwipeThreshold(viewHolder)
    }
}