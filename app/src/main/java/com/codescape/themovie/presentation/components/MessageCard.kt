package com.codescape.themovie.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codescape.themovie.presentation.theme.TheMovieTheme

@Composable
fun MessageCard(
    modifier: Modifier = Modifier,
    title: String,
    buttonText: String,
    onClick: () -> Unit = {}
) {
    Column(
        modifier =
            modifier.padding(
                vertical = 21.dp,
                horizontal = 16.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = TheMovieTheme.typography.bodyMedium,
            color = TheMovieTheme.colors.text
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = buttonText,
            style = TheMovieTheme.typography.titleMedium,
            color = TheMovieTheme.colors.textLink,
            modifier =
                Modifier.clickable {
                    onClick()
                }
        )
    }
}

@Preview
@Composable
fun EmptyCardPreview() {
    TheMovieTheme {
        MessageCard(
            title = "No favorites yet",
            buttonText = "Search"
        )
    }
}
