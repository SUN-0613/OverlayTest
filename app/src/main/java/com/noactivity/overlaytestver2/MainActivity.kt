package com.noactivity.overlaytestver2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity: AppCompatActivity()
{

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

        })

        findViewById<Button>(R.id.button_stop_mini).setOnClickListener(View.OnClickListener
        {
            var intent = Intent(this@MainActivity, MyService::class.java)
            stopService(intent)
        })

    }

    /**
     * static
     */
    companion object
    {
        var Instance: MainActivity? = null
    }

}