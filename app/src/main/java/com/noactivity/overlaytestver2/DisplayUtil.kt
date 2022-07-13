package com.noactivity.overlaytestver2

import android.app.Service
import android.graphics.Point
import android.view.WindowInsets
import android.view.WindowManager
import android.view.WindowMetrics

/**
 * モニタ情報
 */
class DisplayUtil
{

    /**
     * static
     */
    companion object
    {

        /**
         * ウィンドウサイズ取得
         */
        fun getWindowSize(): Point
        {

            val context = MainActivity.Instance!!.applicationContext
            val manager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager

            val point = Point()
            manager.defaultDisplay.getSize(point)

            return point

        }

    }

}