package lt.vitalijus.translator_kmm.android.translate.presentation.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import lt.vitalijus.translator_kmm.android.R
import lt.vitalijus.translator_kmm.android.core.theme.LightBlue
import lt.vitalijus.translator_kmm.core.presentation.UiLanguage

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun TranslateTextField(
    fromText: String,
    toText: String?,
    isTranslating: Boolean,
    fromLanguage: UiLanguage,
    toLanguage: UiLanguage,
    onTranslateClick: () -> Unit,
    onTextChange: (String) -> Unit,
    onCopyClick: (String) -> Unit,
    onCloseClick: () -> Unit,
    onTextFieldClick: () -> Unit,
    onSpeakerClick: () -> Unit,
    isSpeaking: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onTextFieldClick),
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        AnimatedContent(targetState = toText) {
            if (toText == null || isTranslating) {
                IdleTranslateTextField(
                    fromText = fromText,
                    isTranslating = isTranslating,
                    onTextChange = onTextChange,
                    onTranslateClick = onTranslateClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            } else {
                Column(
                    modifier = modifier
                ) {
                    FromTextField(
                        fromText = fromText,
                        fromLanguage = fromLanguage,
                        onCloseClick = onCloseClick
                    )
                    Divider()
                    ToTextField(
                        toText = toText,
                        toLanguage = toLanguage,
                        onCopyClick = onCopyClick,
                        onSpeakerClick = onSpeakerClick,
                        isSpeaking = isSpeaking
                    )
                }
            }
        }
    }
}

@Composable
private fun IdleTranslateTextField(
    fromText: String,
    isTranslating: Boolean,
    onTextChange: (String) -> Unit,
    onTranslateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Box {
            BasicTextField(
                value = fromText,
                onValueChange = onTextChange,
                cursorBrush = SolidColor(MaterialTheme.colors.primary),
                modifier = Modifier
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                textStyle = TextStyle(color = MaterialTheme.colors.onSurface)
            )
            if (fromText.isEmpty() && !isFocused) {
                Text(
                    text = stringResource(id = R.string.enter_a_text_to_translate),
                    color = LightBlue
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp, end = 8.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            ProgressButton(
                text = stringResource(id = R.string.translate),
                isLoading = isTranslating,
                onClick = onTranslateClick,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
private fun FromTextField(
    fromText: String,
    fromLanguage: UiLanguage,
    onCloseClick: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            LanguageItem(language = fromLanguage)
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = stringResource(id = R.string.close),
                    tint = LightBlue
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = fromText,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
private fun ToTextField(
    toText: String,
    toLanguage: UiLanguage,
    onCopyClick: (String) -> Unit,
    onSpeakerClick: () -> Unit,
    isSpeaking: Boolean
) {
    Column(modifier = Modifier.padding(16.dp)) {
        LanguageItem(language = toLanguage)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = toText,
            color = MaterialTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {
                    onCopyClick(toText)
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.copy),
                    contentDescription = stringResource(id = R.string.copy),
                    tint = LightBlue
                )
            }
            SpeakButton(
                onClick = onSpeakerClick,
                isSpeaking = isSpeaking
            )
        }
    }
}
