package org.redesnac.lsgbible.presentation.book.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.redesnac.lsgbible.domain.book.Book

@Composable
fun BookItem(
    book: Book,
    onItemClick:(Book) -> Unit
){
    Card(
        elevation = 3.dp,
        modifier = Modifier
            .clickable(onClick = { onItemClick(book) })
    ) {
        Column (
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 4.dp
            )
                ) {
            Surface(
                modifier = Modifier.align(CenterHorizontally),
            ) {
                Text(
                    text = book.name.first().toString(),
                    style = MaterialTheme.typography.h2,
                )
            }
            Surface(
                modifier = Modifier.align(CenterHorizontally)
            ) {
                Text(
                    text = book.name,
                    style = MaterialTheme.typography.body1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
fun BookItemPreview() {
    val book = Book("Génèse",  40)
    BookItem( book = book, {})
}