package com.hw.demo.androidandflutterinteractive

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.unit.dp
import com.hw.demo.androidandflutterinteractive.FlutterEngineManager.Companion.FLUTTER_ENGINE_ID
import com.hw.demo.androidandflutterinteractive.ui.theme.AndroidAndFlutterInteractiveTheme
import io.flutter.embedding.android.FlutterActivity

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkFlutterEngine(this)

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

//    private fun initFlutterEnding(flutterEngineId: String): FlutterEngine {
//        //创建FlutterEnging
//        val flutterEngine = FlutterEngine(this)
//        //指定要跳转的 Flutter 页面
//        flutterEngine.navigationChannel.setInitialRoute()
//        flutterEngine.dartExecutor.executeDartEntrypoint(
//            DartExecutor.DartEntrypoint.createDefault()
//        )
//        //缓存FlutterEndine
//        val flutterEngineCache = FlutterEngineCache.getInstance()
//        flutterEngineCache.put(flutterEngineId, flutterEngine)
//        return flutterEngine
//    }

    override fun onDestroy() {
        super.onDestroy()
        /**
         * 注意这里一定要销毁，否则会导致内存泄漏
         * 因为 FlutterEngine 比显示它的 FlutterActivity 生命周期要长
         * 当我们退出 FlutterActivity 时，FlutterEngine 可能还会继续执行代码
         * 所以我们应该在 FlutterActivity 退出时调用 flutterEngine.destroy 停止执行并释放资源
         */
        FlutterEngineManager.instance.releaseFlutterEngine(FLUTTER_ENGINE_ID)
    }

    companion object {
        private const val TAG = "MainActivity"

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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                toFlutterActivity(context)
            }) {
                Text(text = "Jump to Flutter!")
            }
            Button(onClick = {
                toSecondActivity(context)
            }) {
                Text(text = "Jump to Second Activity")
            }
        }
    }
}

private fun checkFlutterEngine(context: Context) {
    FlutterEngineManager.instance.getFlutterEngine(context, FLUTTER_ENGINE_ID, "main?{\"name\":\"erdai\",\"age\":18}")
}

fun toSecondActivity(context: Context) {
    context.startActivity(Intent(context, SecondActivity::class.java))
}

fun toFlutterActivity(context: Context) {
//    val intent = FlutterActivity.createDefaultIntent(context)
    checkFlutterEngine(context)
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