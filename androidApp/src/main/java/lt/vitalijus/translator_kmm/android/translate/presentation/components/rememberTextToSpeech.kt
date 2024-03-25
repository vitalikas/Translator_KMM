package lt.vitalijus.translator_kmm.android.translate.presentation.components

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberTextToSpeech(
    onDispose: () -> Unit,
    onDone: () -> Unit
): TextToSpeech {
    val context = LocalContext.current
    val tts = remember {
        TextToSpeech(context, null)
    }
    return tts.apply {
        DisposableEffect(key1 = Unit) {
            onDispose {
                with(this@apply) {
                    onDispose()
                    stop()
                    shutdown()
                }
            }
        }

        setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(p0: String?) {

            }

            override fun onDone(p0: String?) {
                onDone()
            }

            override fun onError(p0: String?) {

            }
        })
    }
}
