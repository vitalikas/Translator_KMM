@file:OptIn(ExperimentalMaterial3Api::class)

package lt.vitalijus.translator_kmm.android.translate.presentation

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.launch
import lt.vitalijus.translator_kmm.android.R
import lt.vitalijus.translator_kmm.android.translate.presentation.components.LanguageDropdown
import lt.vitalijus.translator_kmm.android.translate.presentation.components.LanguageDropdownReverse
import lt.vitalijus.translator_kmm.android.translate.presentation.components.SwapLanguagesButton
import lt.vitalijus.translator_kmm.android.translate.presentation.components.TranslateHistoryItem
import lt.vitalijus.translator_kmm.android.translate.presentation.components.TranslateTextField
import lt.vitalijus.translator_kmm.android.translate.presentation.util.asUiText
import lt.vitalijus.translator_kmm.android.translate.presentation.util.rememberTextToSpeech
import lt.vitalijus.translator_kmm.translate.presentation.translate.TranslateEvent
import lt.vitalijus.translator_kmm.translate.presentation.translate.TranslateState
import java.util.Locale

@Composable
fun TranslateScreen(
    state: TranslateState,
    onEvent: (TranslateEvent) -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            onEvent(TranslateEvent.Refresh)
        }
    }
    LaunchedEffect(state.isRefreshing) {
        if (state.isRefreshing) {
            pullToRefreshState.startRefresh()
        } else {
            pullToRefreshState.endRefresh()
        }
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = state.error) {
        val message = state.error?.asUiText()
        message?.let { errorMessage ->
            Toast.makeText(context, errorMessage.asString(context), Toast.LENGTH_LONG).show()
            onEvent(TranslateEvent.OnErrorSeen)
        }
    }

    val tts = rememberTextToSpeech(
        onDispose = {
            onEvent(TranslateEvent.StopSpeakingText)
        }
    ).apply {
        language = state.toLanguage.toLocale() ?: Locale.ENGLISH

        setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {

            }

            override fun onDone(utteranceId: String?) {
                onEvent(TranslateEvent.StopSpeakingText)
            }

            override fun onError(utteranceId: String?) {
            }
        })
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
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        Box(
            modifier = Modifier
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
        ) {
            LazyColumn(
                state = listState,
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
                        val (dropdown, swapButton, dropdownReverse) = createRefs()

                        LanguageDropdown(
                            language = state.fromLanguage,
                            isOpen = state.isChoosingFromLanguage,
                            onClick = {
                                onEvent(TranslateEvent.OpenFromLanguageDropDown)
                                stopTts(state.isSpeaking, onEvent, tts)
                            },
                            onDismiss = {
                                onEvent(TranslateEvent.StopChoosingLanguage)
                            },
                            onSelectLanguage = { language ->
                                onEvent(TranslateEvent.ChooseFromLanguage(language = language))
                            },
                            modifier = Modifier.constrainAs(dropdown) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }
                        )

                        SwapLanguagesButton(
                            onClick = {
                                onEvent(TranslateEvent.SwapLanguages)
                                stopTts(state.isSpeaking, onEvent, tts)
                            },
                            modifier = Modifier.constrainAs(swapButton) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }
                        )

                        LanguageDropdownReverse(
                            language = state.toLanguage,
                            isOpen = state.isChoosingToLanguage,
                            onClick = {
                                onEvent(TranslateEvent.OpenToLanguageDropDown)
                                stopTts(state.isSpeaking, onEvent, tts)
                            },
                            onDismiss = {
                                onEvent(TranslateEvent.StopChoosingLanguage)
                            },
                            onSelectLanguage = { language ->
                                onEvent(TranslateEvent.ChooseToLanguage(language = language))
                            },
                            modifier = Modifier.constrainAs(dropdownReverse) {
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
                            stopTts(state.isSpeaking, onEvent, tts)
                        },
                        onTextFieldClick = {
                            onEvent(TranslateEvent.EditTranslation)
                            stopTts(state.isSpeaking, onEvent, tts)
                        },
                        onSpeakerClick = {
                            if (!state.isSpeaking) {
                                onEvent(TranslateEvent.StartSpeakingText)
                                tts.speak(
                                    state.toText,
                                    TextToSpeech.QUEUE_FLUSH,
                                    null,
                                    TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID
                                )
                            } else {
                                onEvent(TranslateEvent.StopSpeakingText)
                                tts.stop()
                            }
                        },
                        isSpeaking = state.isSpeaking,
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
                            coroutineScope.launch {
                                listState.scrollToItem(0)
                            }
                            onEvent(TranslateEvent.SelectHistoryItem(item = item))
                            stopTts(state.isSpeaking, onEvent, tts)
                        }
                    )
                }
            }

            PullToRefreshContainer(
                state = pullToRefreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter),
                containerColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            )
        }
    }
}

private fun stopTts(
    isSpeaking: Boolean,
    onEvent: (TranslateEvent) -> Unit,
    tts: TextToSpeech
) {
    if (!isSpeaking) {
        return
    }

    onEvent(TranslateEvent.StopSpeakingText)
    tts.stop()
}
