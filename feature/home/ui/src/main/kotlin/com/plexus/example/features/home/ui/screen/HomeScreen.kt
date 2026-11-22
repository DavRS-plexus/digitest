package com.plexus.example.features.home.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.plexus.example.core.resources.R
import com.plexus.example.core.theme.AppTheme
import com.plexus.example.core.theme.color.LocalCustomColors
import com.plexus.example.core.theme.dimen.Spacing
import com.plexus.example.core.theme.font.LocalTypography

@Composable
internal fun HomeScreen(onClickStart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.background)
            .padding(Spacing.spacing_04),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.home_welcome),
            style = LocalTypography.headlineSmall,
            textAlign = TextAlign.Center,
        )
        Button(onClick = onClickStart, modifier = Modifier.padding(top = Spacing.spacing_04)) {
            Text(stringResource(R.string.home_start))
        }
    }
}

@Preview(name = "Home", showBackground = true)
@Preview(name = "Home - font 130%", showBackground = true, fontScale = 1.3f)
@Preview(name = "Home - font 200%", showBackground = true, fontScale = 2f)
@Composable
private fun HomeScreenPreview() {
    AppTheme(dynamicColor = false) {
        HomeScreen(onClickStart = {})
    }
}
