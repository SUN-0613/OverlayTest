package com.noactivity.overlaytestver2

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat.startActivityForResult


class MyService: Service()
{

    private var view: View? = null
    private var windowManager: WindowManager? = null
    private var layoutParams: WindowManager.LayoutParams? = null
    private var isLongClicked: Boolean = false

    /**
     * サービスをBindして実行する場合は{}内に処理を記述
     */
    override fun onBind(p0: Intent?): IBinder?
    {
        return null
    }

    /**
     * サービス開始
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {

        //return super.onStartCommand(intent, flags, startId)

        if (Build.VERSION.SDK_INT >= 23)
        {

            /* android 6 以上 */
            if (!Settings.canDrawOverlays(this))
            {
                val intent2 = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(MainActivity.Instance!!, intent2, 1000, null)
            }
        }

        addMiniFilter()
        return START_STICKY

    }

    /**
     * インスタンス解放
     */
    override fun onDestroy()
    {

        super.onDestroy()

        windowManager?.removeView(view)

    }

    /**
     * Tap位置表示
     */
    private fun addMiniFilter()
    {

        val context = MainActivity.Instance!!.applicationContext
        val layout = LayoutInflater.from(context)
        val size = DisplayUtil.getWindowSize()
        val width = size.x
        val height = size.y

        layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        )

        layoutParams!!.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                             WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        view = layout.inflate(R.layout.layout_filter_mini, null)
        view!!.setBackgroundColor(Color.argb(100, 0, 0, 0))
        windowManager!!.addView(view, layoutParams)

        /*

        view!!.setOnTouchListener(View.OnTouchListener { v, event ->
            when (event.action)
            {
                MotionEvent.ACTION_DOWN -> { isLongClicked = true }
                MotionEvent.ACTION_MOVE -> { onActionMove(width, height, layoutParams!!, event) }
                MotionEvent.ACTION_UP -> { onActionUp() }
                else -> {}
            }

            return@OnTouchListener false

        })

         */

    }

    /**
     * Tap位置移動イベント
     */
    private fun onActionMove(width: Int, height: Int, params: WindowManager.LayoutParams, event: MotionEvent)
    {

        if (isLongClicked)
        {

            val x = event.getRawX()
            val y = event.getRawY()
            val centerX = (x - (width / 2)).toInt()
            val centerY = (y - (height / 2)).toInt()

            params.x = centerX
            params.y = centerY

            windowManager?.updateViewLayout(view, params)

        }

    }

    /**
     * Tap.Upイベント
     */
    private fun onActionUp()
    {
        isLongClicked = false
    }

}