package lt.vitalijus.translator_kmm.android.translate.presentation.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import lt.vitalijus.translator_kmm.android.R
import lt.vitalijus.translator_kmm.android.core.theme.LightBlue

@Composable
fun SpeakButton(
    isSpeaking: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = {
            onClick()
        }
    ) {
        val drawableId = if (isSpeaking) R.drawable.stop else R.drawable.speaker
        Icon(
            imageVector = ImageVector.vectorResource(id = drawableId),
            contentDescription = stringResource(id = R.string.play_loud),
            tint = LightBlue
        )
    }
}
