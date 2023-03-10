package org.redesnac.lsgbible.presentation.components


import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.redesnac.lsgbible.R
import org.redesnac.lsgbible.presentation.Screen
import org.redesnac.lsgbible.presentation.viewmodels.BooksViewModel


val tag: String = "BooksScreen"

@Composable
fun BooksScreen(
    navController: NavController,
    viewModel: BooksViewModel = hiltViewModel()
) {
    val state = viewModel.readOnlyState.value
    Log.d(tag, "state: $state")
    BackHandler(enabled = state.book!= null){
        viewModel.onBookCliked(null)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            var title:String? = null
            if(state.book != null)
            {
                title = state.book.name
            }
            TopAppBar(
                title = { Text(
                    text = title?:stringResource(id = R.string.app_name)
                ) }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize().background(
            if(state.book != null) Color.White else MaterialTheme.colors.primaryVariant
        )){
            LazyVerticalGrid(
                columns =  if(state.book != null) GridCells.Adaptive(60.dp) else GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if(state.book != null)
                {
                    for (item in  1..state.book.chapters)
                    {
                        item()
                        {
                            ChapterItem(item, onItemClick = {
                                Log.d(tag, "url : ?bookName=${state.book.name} $item")
                                navController.navigate(
                                    Screen.VersesScreen.route + "?bookName=${state.book.name} $item"
                                )
                            })
                        }
                    }
                }
                else {
                    state.books.forEach { book ->
                        item()
                        {
                            BookItem(book = book, onItemClick = {
                                viewModel.onBookCliked(book)
                            })
                        }
                    }
                }

            }
            if(state.error.isNotBlank())
            {
                Text(
                    text = state.error,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(24.dp)
                        .align(Alignment.Center)
                )
            }

            if(state.isLoading)
            {
                CircularProgressIndicator(modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Center))
            }
        }
    }
}