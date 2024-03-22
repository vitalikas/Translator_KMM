@file:OptIn(ExperimentalComposeUiApi::class)

package lt.vitalijus.translator_kmm.android.translate.presentation

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import lt.vitalijus.translator_kmm.android.R
import lt.vitalijus.translator_kmm.android.translate.presentation.components.LanguageDropdown
import lt.vitalijus.translator_kmm.android.translate.presentation.components.LanguageDropdownReverse
import lt.vitalijus.translator_kmm.android.translate.presentation.components.SwapLanguagesButton
import lt.vitalijus.translator_kmm.android.translate.presentation.components.TranslateHistoryItem
import lt.vitalijus.translator_kmm.android.translate.presentation.components.TranslateTextField
import lt.vitalijus.translator_kmm.android.translate.presentation.components.rememberTextToSpeech
import lt.vitalijus.translator_kmm.core.domain.util.TranslateError
import lt.vitalijus.translator_kmm.translate.presentation.translate.TranslateEvent
import lt.vitalijus.translator_kmm.translate.presentation.translate.TranslateState
import java.util.Locale

@Composable
fun TranslateScreen(
    state: TranslateState,
    onEvent: (TranslateEvent) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = state.error) {
        val message = when (state.error) {
            TranslateError.SERVICE_UNAVAILABLE -> context.getString(R.string.error_service_unavailable)
            TranslateError.CLIENT_ERROR -> context.getString(R.string.error_client)
            TranslateError.SERVER_ERROR -> context.getString(R.string.error_server)
            TranslateError.UNKNOWN_ERROR -> context.getString(R.string.error_unknown)
            else -> null
        }
        message?.let { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            onEvent(TranslateEvent.OnErrorSeen)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(TranslateEvent.RecordAudio)
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.mic),
                    contentDescription = stringResource(id = R.string.record_audio)
                )

            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(
                    start = 16.dp,
                    end = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    val (dropdownStart, swapButton, dropdownEnd) = createRefs()

                    LanguageDropdown(
                        language = state.fromLanguage,
                        isOpen = state.isChoosingFromLanguage,
                        onClick = {
                            onEvent(TranslateEvent.OpenFromLanguageDropDown)
                        },
                        onDismiss = {
                            onEvent(TranslateEvent.StopChoosingLanguage)
                        },
                        onSelectLanguage = { language ->
                            onEvent(TranslateEvent.ChooseFromLanguage(language = language))
                        },
                        modifier = Modifier.constrainAs(dropdownStart) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                    )

                    SwapLanguagesButton(
                        onClick = {
                            onEvent(TranslateEvent.SwapLanguages)
                        },
                        modifier = Modifier.constrainAs(swapButton) {
                            start.linkTo(dropdownStart.start)
                            end.linkTo(dropdownEnd.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                    )

                    LanguageDropdownReverse(
                        language = state.toLanguage,
                        isOpen = state.isChoosingToLanguage,
                        onClick = {
                            onEvent(TranslateEvent.OpenToLanguageDropDown)
                        },
                        onDismiss = {
                            onEvent(TranslateEvent.StopChoosingLanguage)
                        },
                        onSelectLanguage = { language ->
                            onEvent(TranslateEvent.ChooseToLanguage(language = language))
                        },
                        modifier = Modifier.constrainAs(dropdownEnd) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                    )
                }
            }

            item {
                val clipboardManager = LocalClipboardManager.current
                val keyboardController = LocalSoftwareKeyboardController.current
                val tts = rememberTextToSpeech()
                TranslateTextField(
                    fromText = state.fromText,
                    toText = state.toText,
                    isTranslating = state.isTranslating,
                    fromLanguage = state.fromLanguage,
                    toLanguage = state.toLanguage,
                    onTranslateClick = {
                        keyboardController?.hide()
                        onEvent(TranslateEvent.Translate)
                    },
                    onTextChange = { text ->
                        onEvent(TranslateEvent.ChangeTranslationText(text = text))
                    },
                    onCopyClick = { text ->
                        clipboardManager.setText(
                            buildAnnotatedString {
                                append(text)
                            }
                        )
                        Toast.makeText(
                            context,
                            context.getText(R.string.copied_to_clipboard),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onCloseClick = {
                        onEvent(TranslateEvent.CloseTranslation)
                    },
                    onSpeakerClick = {
                        tts.apply {
                            language = state.toLanguage.toLocale() ?: Locale.ENGLISH
                            speak(
                                state.toText,
                                TextToSpeech.QUEUE_FLUSH,
                                null,
                                null
                            )
                        }
                    },
                    onTextFieldClick = {
                        onEvent(TranslateEvent.EditTranslation)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                if (state.history.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.history),
                        style = MaterialTheme.typography.h2
                    )
                }
            }

            items(state.history) { item ->
                TranslateHistoryItem(
                    item = item,
                    onClick = {
                        onEvent(TranslateEvent.SelectHistoryItem(item = item))
                    }
                )
            }
        }
    }
}
