package lt.vitalijus.translator_kmm.android.translate.presentation.components

import android.speech.tts.TextToSpeech
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberTextToSpeech(): TextToSpeech {
    val context = LocalContext.current
    val tts = remember {
        TextToSpeech(context, null)
    }
    return tts.apply {
        DisposableEffect(key1 = this) {
            onDispose {
                with(this@apply) {
                    stop()
                    shutdown()
                }
            }
        }
    }
}
