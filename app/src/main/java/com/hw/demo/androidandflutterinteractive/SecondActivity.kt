package com.hw.demo.androidandflutterinteractive

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hw.demo.androidandflutterinteractive.FlutterEngineManager.Companion.FLUTTER_ENGINE_ID
import com.hw.demo.androidandflutterinteractive.databinding.ActivitySecondBinding
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.StringCodec
import java.util.Timer
import kotlin.concurrent.timerTask

class SecondActivity : AppCompatActivity() {

    private lateinit var flutterEngine: FlutterEngine

    private lateinit var flutterFragment: FlutterFragment

    private lateinit var methodChannel: MethodChannel

    private lateinit var eventChannel: EventChannel

    private var eventSink: EventChannel.EventSink? = null

    private lateinit var messageChannel: BasicMessageChannel<String>

    private var count = 0

    private var timer: Timer? = null

    private var eventTimer: Timer? = null

    private var electricity = 0

    private val binding: ActivitySecondBinding by lazy {
        ActivitySecondBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        flutterEngine =
            FlutterEngineManager.instance.getFlutterEngine(this, FLUTTER_ENGINE_ID, "/secondPage?{\"name\":\"secondErdai\",\"age\":28}")

//        methodChannel = MethodChannel(flutterEngine.dartExecutor, "com.hw.demo.androidandflutterinteractive")
//        methodChannel.setMethodCallHandler { call, result ->
//            Log.i(TAG, "MethodCallHandler method = ${call.method}" +
//                    ", arguments = ${call.arguments}" +
//                    ", result = $result")
//            if ("sendFinish" == call.method) {
//                finish()
//            }
//        }

//        eventChannel = EventChannel(flutterEngine.dartExecutor, "com.hw.demo.androidandflutterinteractive.eventchannel")
//        eventChannel.setStreamHandler(object : EventChannel.StreamHandler {
//            override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
//                Log.i(TAG, "eventChannel onListen arguments = $arguments")
//                eventSink = events
//
//                startEventTimer()
//            }
//
//            override fun onCancel(arguments: Any?) {
//                Log.i(TAG, "eventChannel onCancel arguments = $arguments")
//            }
//
//        })

        messageChannel = BasicMessageChannel(flutterEngine.dartExecutor, "com.hw.demo.androidandflutterinteractive.messagechannel", StringCodec.INSTANCE)
        messageChannel.setMessageHandler { message, reply ->
            Log.i(TAG, "messageChannel in MessageHandler message = $message")
            Toast.makeText(this, message?:"null",Toast.LENGTH_SHORT).show()
            reply.reply("梧桐山")
        }

        messageChannel.send("周末去爬山吗？") { reply ->
            Log.i(TAG, "messageChannel onSend message = $reply")
            Toast.makeText(this,reply?:"null",Toast.LENGTH_SHORT).show()
        }

        flutterFragment = FlutterFragment.withCachedEngine(FLUTTER_ENGINE_ID).build()

        supportFragmentManager.beginTransaction().replace(binding.fragmentContainerFl.id, flutterFragment).commit()

//        startTimer()
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

    private fun startEventTimer() {
        Log.i(TAG, "startEventTimer")
        eventTimer?.cancel()
        eventTimer = Timer()
        eventTimer?.schedule(timerTask {
            runOnUiThread {
                electricity += 20
                Log.i(TAG, "eventTimer electricity = $electricity")
                eventSink?.success("电量：$electricity %")
                if (electricity >= 1000) {
                    eventSink?.endOfStream()
                }
            }
        }, 0,1000)
    }

    private fun stopEventTimer() {
        Log.i(TAG, "stopEventTimer")
        eventTimer?.cancel()
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
        stopEventTimer()
        flutterFragment.onDestroy()
        FlutterEngineManager.instance.releaseFlutterEngine(FLUTTER_ENGINE_ID)
    }

    companion object {
        private const val TAG = "SecondActivity"
    }
}