package org.redesnac.lsgbible.presentation.verse

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import org.redesnac.lsgbible.R
import org.redesnac.lsgbible.presentation.book.tag

@Composable
fun VersesScreen(
    viewModel: VersesViewModel = hiltViewModel()
){
    val state = viewModel.readOnlyState.value
    Log.d(tag, "state: $state")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            var title:String? = null
            if (state.bookName.isNotEmpty())
            {
                title = state.bookName
            }
            TopAppBar(
                title = { Text(
                    text = title?: stringResource(id = R.string.app_name)
                ) }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {

                state.verses.forEach { verse ->
                    item()
                    {
                        VerseItem(verse = verse)
                    }
                }
            }

            if (state.error.isNotBlank()) {
                Text(
                    text = state.error,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(24.dp)
                        .align(Alignment.Center)
                )
            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}