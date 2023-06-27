package org.redesnac.lsgbible.presentation.book.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ChapterItem(
    chapter: Long,
    onItemClick:(Long) -> Unit
){
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colors.secondary)
            .clickable(onClick = { onItemClick(chapter) },
            )
    ) {
        Text(
            text = chapter.toString(),
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
fun ChapterItemPreview() {
    ChapterItem( 1, {})
}