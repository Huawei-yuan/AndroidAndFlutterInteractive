package com.hw.demo.androidandflutterinteractive

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.hw.demo.androidandflutterinteractive.FlutterEngineManager.Companion.FLUTTER_ENGINE_ID
import com.hw.demo.androidandflutterinteractive.databinding.ActivitySecondBinding
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.engine.FlutterEngine

class SecondActivity : AppCompatActivity() {

    private lateinit var flutterEngine: FlutterEngine

    private lateinit var flutterFragment: FlutterFragment

    private val binding: ActivitySecondBinding by lazy {
        ActivitySecondBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        flutterEngine =
            FlutterEngineManager.instance.getFlutterEngine(this, FLUTTER_ENGINE_ID, "main?{\"name\":\"secondErdai\",\"age\":28}")

        flutterFragment = FlutterFragment.withCachedEngine(FLUTTER_ENGINE_ID).build()

        supportFragmentManager.beginTransaction().replace(binding.fragmentContainerFl.id, flutterFragment).commit()
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
        flutterFragment.onDestroy()
        FlutterEngineManager.instance.releaseFlutterEngine(FLUTTER_ENGINE_ID)
    }
}