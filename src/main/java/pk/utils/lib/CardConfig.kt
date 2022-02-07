package pk.utils.lib

/**
 * Author: pk
 * Time: 2021/9/14  2:44 下午
 * Description:
 */

class CardConfig {

    companion object{
        /**
         * 显示可见的卡片数量
         */
        val DEFAULT_SHOW_ITEM = 3

        /**
         * 默认缩放的比例
         */
        val DEFAULT_SCALE = 0.1f

        /**
         * 卡片Y轴偏移量时按照14等分计算
         */
        val DEFAULT_TRANSLATE_Y = 14

        /**
         * 卡片滑动时默认倾斜的角度
         */
        val DEFAULT_ROTATE_DEGREE = 20f

        /**
         * 卡片滑动时不偏左也不偏右
         */
        val SWIPING_NONE = 1

        /**
         * 卡片向左滑动时
         */
        val SWIPING_LEFT = 1 shl 2

        /**
         * 卡片向右滑动时
         */
        val SWIPING_RIGHT = 1 shl 3

        /**
         * 卡片从左边滑出
         */
        val SWIPED_LEFT = 1

        /**
         * 卡片从右边滑出
         */
        val SWIPED_RIGHT = 1 shl 2
    }


}

