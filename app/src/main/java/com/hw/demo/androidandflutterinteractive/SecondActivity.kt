package com.hw.demo.androidandflutterinteractive

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.hw.demo.androidandflutterinteractive.FlutterEngineManager.Companion.FLUTTER_ENGINE_ID
import com.hw.demo.androidandflutterinteractive.databinding.ActivitySecondBinding
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import java.util.Timer
import kotlin.concurrent.timerTask

class SecondActivity : AppCompatActivity() {

    private lateinit var flutterEngine: FlutterEngine

    private lateinit var flutterFragment: FlutterFragment

    private lateinit var methodChannel: MethodChannel

    private var count = 0

    private var timer: Timer? = null

    private val binding: ActivitySecondBinding by lazy {
        ActivitySecondBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        flutterEngine =
            FlutterEngineManager.instance.getFlutterEngine(this, FLUTTER_ENGINE_ID, "main?{\"name\":\"secondErdai\",\"age\":28}")

        methodChannel = MethodChannel(flutterEngine.dartExecutor, "com.hw.demo.androidandflutterinteractive")
        methodChannel.setMethodCallHandler { call, result ->
            Log.i(TAG, "MethodCallHandler method = ${call.method}" +
                    ", arguments = ${call.arguments}" +
                    ", result = $result")
            if ("sendFinish" == call.method) {
                finish()
            }
        }
        flutterFragment = FlutterFragment.withCachedEngine(FLUTTER_ENGINE_ID).build()

        supportFragmentManager.beginTransaction().replace(binding.fragmentContainerFl.id, flutterFragment).commit()

        startTimer()
    }

    private fun startTimer() {
        Log.i(TAG, "startTimer")
        timer?.cancel()
        timer = Timer()
        timer?.schedule(timerTask {
            runOnUiThread {
                val map = mapOf("count" to count++)
                Log.i(TAG , "timer count = $count")
                methodChannel.invokeMethod("timer", map)
            }
        }, 0, 1000)
    }

    private fun stopTimer() {
        Log.i(TAG, "stopTimer")
        timer?.cancel()
    }

    override fun onPostResume() {
        super.onPostResume()
        flutterFragment.onPostResume()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        flutterFragment.onNewIntent(intent?: Intent())
    }

    override fun onStart() {
        super.onStart()
        flutterFragment.onStart()
    }


    override fun onResume() {
        super.onResume()
        flutterFragment.onResume()
    }

    override fun onPause() {
        super.onPause()
        flutterFragment.onPause()
    }

    override fun onStop() {
        super.onStop()
        flutterFragment.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
        flutterFragment.onDestroy()
        FlutterEngineManager.instance.releaseFlutterEngine(FLUTTER_ENGINE_ID)
    }

    companion object {
        private const val TAG = "SecondActivity"
    }
}