package com.noactivity.overlaytestver2

import android.content.Context
import android.content.Intent
import android.hardware.input.InputManager
import android.os.Bundle
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.InputDevice
import android.view.InputEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.TimeUnit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noactivity.overlaytestver2.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity: AppCompatActivity()
{

    private var isRun: Boolean = false

    private var isShowOverlay: Boolean = false

    /**
     * 初期化
     */
    init
    {
        Instance = this
    }

    /**
     * 初回実行
     */
    override fun onCreate(savedInstanceState: Bundle?)
    {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button_start_mini).setOnClickListener(View.OnClickListener
        {

            var intent = Intent(this@MainActivity, MyService::class.java)
            startService(intent)

            isShowOverlay = true

        })

        findViewById<Button>(R.id.button_stop_mini).setOnClickListener(View.OnClickListener
        {

            var intent = Intent(this@MainActivity, MyService::class.java)
            stopService(intent)

            isRun = false
            isShowOverlay = false

        })

        findViewById<Button>(R.id.button_tap).setOnClickListener(View.OnClickListener
        {
            sendTap()
        })

    }

    fun sendTap()
    {

        if (isShowOverlay)
        {

            isRun = true

            GlobalScope.launch(Dispatchers.Default)
            {
                run()
            }

        }

    }

    private fun run()
    {

        val seconds: Int = 2000
        var raiseEvent = SystemClock.uptimeMillis() + seconds

        while (isRun)
        {

            val ms = SystemClock.uptimeMillis()

            if (ms >= raiseEvent)
            {
                sendTapEvent()
                raiseEvent = SystemClock.uptimeMillis() + seconds
            }

        }

    }

    /**
     * Tapイベントを発行する
     */
    private fun sendTapEvent()
    {

        var metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        val pointX = metrics.widthPixels / 2f
        val pointY = metrics.heightPixels / 2f

        val downTime = SystemClock.uptimeMillis()
        val eventTime = SystemClock.uptimeMillis() + 1000

        val event1 = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, pointX, pointY, 0)
        event1.source = InputDevice.SOURCE_MOUSE
        injectInputEvent(event1)

        TimeUnit.SECONDS.sleep(1)

        val event2 = MotionEvent.obtain(eventTime + 100, eventTime + 2000, MotionEvent.ACTION_UP, pointX, pointY, 0)
        event2.source = InputDevice.SOURCE_MOUSE
        injectInputEvent(event2)

    }

    /**
     * 入力イベント
     */
    private fun injectInputEvent(event: InputEvent)
    {

        try
        {

            val inputManager = getSystemService(Context.INPUT_SERVICE) as InputManager
            val injectInputEventMethod = InputManager::class.java.getMethod("injectInputEvent", InputEvent::class.java, Int::class.javaPrimitiveType)

            injectInputEventMethod.invoke(inputManager, event, 2)

            println("Tap OK")

        }
        catch (e: NoSuchMethodException)
        {
            e.printStackTrace()
        }
        catch (e: IllegalAccessException)
        {
            e.printStackTrace()
        }
        catch (e: IllegalArgumentException)
        {
            e.printStackTrace()
        }
        catch (e: InvocationTargetException)
        {
            e.printStackTrace()
        }

    }

    /**
     * static
     */
    companion object
    {
        var Instance: MainActivity? = null
    }

}