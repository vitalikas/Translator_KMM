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
    isSpeak: Boolean,
    onClick: (isSpeak: Boolean) -> Unit
) {

    IconButton(
        onClick = { onClick(!isSpeak) }
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = if (!isSpeak) R.drawable.speaker else R.drawable.stop),
            contentDescription = stringResource(id = R.string.play_loud),
            tint = LightBlue
        )
    }
}
