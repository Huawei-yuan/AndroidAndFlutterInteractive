package com.hw.demo.androidandflutterinteractive

import android.content.Context
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

class FlutterEngineManager private constructor(){

    fun getFlutterEngine(context: Context, flutterEngineId: String, route: String?): FlutterEngine {
        return FlutterEngineCache.getInstance().get(flutterEngineId) ?: createFlutterEngine(context, flutterEngineId, route)
    }
    private fun createFlutterEngine(context: Context, flutterEngineId: String, route: String?): FlutterEngine {
        val flutterEngine = FlutterEngine(context)
        //指定要跳转的 Flutter 页面
        route?.let {
            flutterEngine.navigationChannel.setInitialRoute(it)
        }
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )
        FlutterEngineCache.getInstance().put(flutterEngineId, flutterEngine)
        return flutterEngine
    }
    fun releaseFlutterEngine(flutterEngineId: String) {
        FlutterEngineCache.getInstance().get(flutterEngineId)?.let { flutterEngine ->
            FlutterEngineCache.getInstance().remove(flutterEngineId)
            flutterEngine.destroy()
        }
    }

    companion object {
        val FLUTTER_ENGINE_ID = "default"


        val instance: FlutterEngineManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            FlutterEngineManager()
        }
    }
}