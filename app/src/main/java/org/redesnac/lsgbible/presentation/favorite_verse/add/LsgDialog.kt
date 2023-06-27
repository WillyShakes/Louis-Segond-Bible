package org.redesnac.lsgbible.presentation.favorite_verse.add

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import org.redesnac.lsgbible.R
import org.redesnac.lsgbible.data.utils.Constants.BOOKS
import org.redesnac.lsgbible.data.utils.Constants.NUMBERS
import org.redesnac.lsgbible.presentation.theme.LSGBibleTheme

@Composable
fun AddFavoriteVersesScreen(
    viewModel: AddFavoriteVerseViewModel = hiltViewModel(),
    dialogState: MutableState<Boolean>,
) {
    val TAG = "AddFavoriteVersesScreen"
    AddFavoriteVerseDialog(
        addVersesState = viewModel.readOnlyUiState.collectAsState(),
        onOkButtonClicked = {
            Log.d(TAG, "onOkButtonClicked")
            viewModel.onOkButtonClicked()
                            },
        onSelectedBook = { book -> viewModel.onBookSelected(book) },
        onSelectedChapter = { chapter -> viewModel.onChapterSelected(chapter) },
        onSelectedVerse= { verse -> viewModel.onVerseSelected(verse) },
        dialogState = dialogState,
    )
}


@Composable
fun LsgDialog(
    title: String,
    dialogState: MutableState<Boolean>,
    isOkButtonEnabled: Boolean = false,
    onOkButtonClicked: () -> Unit,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxHeight(0.9f)
            .fillMaxWidth(1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TitleAndButton(title, dialogState)
            AddBody(content)
            BottomButtons(isOkButtonEnabled = isOkButtonEnabled, dialogState = dialogState, onOkButtonClicked = onOkButtonClicked)
        }
    }
}

@Composable
private fun TitleAndButton(title: String, dialogState: MutableState<Boolean>) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 24.sp)
            IconButton(modifier = Modifier.then(Modifier.size(24.dp)),
                onClick = {
                    dialogState.value = false
                }) {
                Icon(
                    Icons.Filled.Close,
                    "contentDescription"
                )
            }
        }
        Divider(color = Color.DarkGray, thickness = 1.dp)
    }
}

@Composable
private fun BottomButtons(
    dialogState: MutableState<Boolean>,
    isOkButtonEnabled: Boolean,
    onOkButtonClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onOkButtonClicked,
            modifier = Modifier
                .padding(end = 5.dp),
            shape = RoundedCornerShape(16.dp),
            enabled = isOkButtonEnabled
        ) {
            Text(text = stringResource(R.string.annuler).uppercase(), fontSize = 20.sp)
        }
        Button(
            onClick = {
                dialogState.value = false
            },
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = stringResource(R.string.ok).uppercase(), fontSize = 20.sp)
        }
    }
}

@Composable
private fun AddBody(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        content()
    }
}

@Preview
@Composable
fun DialogPreview() {
    LSGBibleTheme {
        LsgDialog(
            title = stringResource(R.string.add_a_favorite_verse),
            dialogState = remember { mutableStateOf(true) },
            onOkButtonClicked = {}
        ){
        }
    }
}

@Composable
fun AddFavoriteVerseDialog(
    addVersesState: State<AddVersesUiState> = mutableStateOf(AddVersesUiState()),
    onOkButtonClicked: () -> Unit,
    onSelectedBook: (String) -> Unit,
    onSelectedChapter: (String) -> Unit,
    onSelectedVerse: (String) -> Unit,
    dialogState: MutableState<Boolean>,
) {
// Context to toast a message
    val ctx: Context = LocalContext.current

    // Code to Show and Dismiss Dialog
    if (dialogState.value) {
        Dialog(
            onDismissRequest = { dialogState.value = false },
            content = {
                LsgDialog(
                    title = stringResource(R.string.add_a_favorite_verse),
                    dialogState = dialogState,
                    isOkButtonEnabled = addVersesState.value.isOkButtonVisible,
                    onOkButtonClicked = onOkButtonClicked,
                ){
                    AddFavoriteVerseDialogContent(
                        onSelectedBook = onSelectedBook,
                        onSelectedChapter = onSelectedChapter,
                        onSelectedVerse = onSelectedVerse
                    )
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        )
    } else {
        Toast.makeText(ctx, "Dialog Closed", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun AddFavoriteVerseDialogContent(
    onSelectedBook: (String) -> Unit,
    onSelectedChapter: (String) -> Unit,
    onSelectedVerse: (String) -> Unit,
) {
    Column {
        ExposedDropdownMenu(BOOKS, stringResource(R.string.book), onSelectedBook)
        ExposedDropdownMenu(NUMBERS, stringResource(R.string.chapter), onSelectedChapter)
        ExposedDropdownMenu(NUMBERS, stringResource(R.string.verse), onSelectedVerse)
    }
}

@Preview
@Composable
fun AddFavoriteVerseDialogContentPreview() {
    LSGBibleTheme {
        AddFavoriteVerseDialogContent({},{},{})
    }
}

@Preview
@Composable
fun AddFavoriteVerseDialogPreview() {
    LSGBibleTheme {
        AddFavoriteVerseDialog(
            onOkButtonClicked = {},
            onSelectedBook =  {},
            onSelectedChapter =  {},
            onSelectedVerse =  {},
            dialogState = remember { mutableStateOf(false) }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExposedDropdownMenu(
    options: List<String>,
    label: String,
    onSelectedOption: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }
// We want to react on tap/press on TextField to show menu
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            readOnly = true,
            value = selectedOptionText,
            onValueChange = { },
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                        onSelectedOption(selectionOption)
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}