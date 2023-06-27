package org.redesnac.lsgbible.presentation.favorite_verse.list

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.redesnac.lsgbible.R
import org.redesnac.lsgbible.domain.verse.Verse
import org.redesnac.lsgbible.presentation.favorite_verse.add.AddFavoriteVersesScreen
import org.redesnac.lsgbible.presentation.theme.LSGBibleTheme
import org.redesnac.lsgbible.presentation.verse.VerseItem


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteVersesScreen(
    viewModel: FavoriteVersesViewModel = hiltViewModel(),
) {
    val ctx: Context = LocalContext.current
    val TAG = "FavoriteVersesScreen"
    val state = viewModel.state.collectAsState().value
    Log.d(TAG, "state: $state")
    val dialogState: MutableState<Boolean> = remember { mutableStateOf(false) }
    Log.d(TAG, "dialogState: ${dialogState.value}")
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.favorite_verses)
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    dialogState.value = true
                },
                modifier = Modifier
                    .pointerInput(Unit){
                        detectTapGestures(
                            onLongPress = {
                                viewModel.onAddButtonLongClicked()
                            }
                        )
                    }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add FAB",
                    tint = Color.White,
                )
            }
        }
    )
    { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (dialogState.value) {
                AddFavoriteVersesScreen(dialogState = dialogState)
            }

            when (state) {
                is VerseViewState.loading -> LoadingScreen()
                is VerseViewState.onVerseLoaded -> VersesScreen(list = state.list)
                is VerseViewState.toast ->  Toast.makeText(ctx, "Dialog Closed", Toast.LENGTH_SHORT).show()
                else -> {}
            }
        }
    }
}

@Composable
fun VersesScreen(list: List<VerseViewStateItem>) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
    ) {
        list.forEach { verseViewStateItem ->
            item()
            {
                val verse = verseViewStateItem as VerseViewStateItem.Verse
                VerseItem(verse = Verse(
                    chapter = verse.chapter,
                    verse = verse.verseNumber,
                    text = ""
                ))
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize())
    {
        CircularProgressIndicator(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Center)
        )
    }
}

@Preview
@Composable
fun FavoriteVersesScreenPreview() {
    LSGBibleTheme {
        FavoriteVersesScreen()
    }
}