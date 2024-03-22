@file:OptIn(ExperimentalAnimationApi::class)

package lt.vitalijus.translator_kmm.android.translate.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import lt.vitalijus.translator_kmm.android.R

@Composable
fun ProgressButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100))
            .background(MaterialTheme.colors.primary)
            .clickable(onClick = onClick)
            .padding(start = 16.dp, end = 16.dp)
            .size(width = 96.dp, height = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(targetState = isLoading) { isLoading ->
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colors.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                IconButton(
                    onClick = onClick,
                    modifier = modifier
                        .background(
                            color = MaterialTheme.colors.primary,
                            shape = CircleShape
                        )
                ) {
                    Row {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.translate),
                            contentDescription = stringResource(id = R.string.swap_languages),
                            tint = MaterialTheme.colors.onPrimary
                        )

                        Spacer(Modifier.width(10.dp))

                        Text(
                            text = text,
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
                }
            }
        }
    }
}
