package com.hw.demo.androidandflutterinteractive

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.hw.demo.androidandflutterinteractive.MainActivity.Companion.FLUTTER_ENGINE_ID
import com.hw.demo.androidandflutterinteractive.ui.theme.AndroidAndFlutterInteractiveTheme
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

class MainActivity : ComponentActivity() {

    private lateinit var flutterEngine: FlutterEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flutterEngine = initFlutterEnding(FLUTTER_ENGINE_ID)

        setContent {
            AndroidAndFlutterInteractiveTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyScreen()
                }
            }
        }
    }

    private fun initFlutterEnding(flutterEngineId: String): FlutterEngine {
        //创建FlutterEnging
        val flutterEngine = FlutterEngine(this)
        //指定要跳转的 Flutter 页面
        flutterEngine.navigationChannel.setInitialRoute("main?{\"name\":\"erdai\",\"age\":18}")
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )
        //缓存FlutterEndine
        val flutterEngineCache = FlutterEngineCache.getInstance()
        flutterEngineCache.put(flutterEngineId, flutterEngine)
        return flutterEngine
    }

    override fun onDestroy() {
        super.onDestroy()
        /**
         * 注意这里一定要销毁，否则会导致内存泄漏
         * 因为 FlutterEngine 比显示它的 FlutterActivity 生命周期要长
         * 当我们退出 FlutterActivity 时，FlutterEngine 可能还会继续执行代码
         * 所以我们应该在 FlutterActivity 退出时调用 flutterEngine.destroy 停止执行并释放资源
         */
        flutterEngine.destroy()
    }

    companion object {
        private const val TAG = "MainActivity"

        const val FLUTTER_ENGINE_ID = "default"
    }
}

@Composable
fun MyScreen() {
    Greeting("Android", context = LocalContext.current)
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, context: Context) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Button(onClick = {
            toFlutterActivity(context)
        }) {
            Text(text = "Jump to Flutter!")
        }
    }
}

fun toFlutterActivity(context: Context) {
//    val intent = FlutterActivity.createDefaultIntent(context)
    val intent = FlutterActivity.withCachedEngine(FLUTTER_ENGINE_ID).build(context)
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidAndFlutterInteractiveTheme {
        MyScreen()
    }
}