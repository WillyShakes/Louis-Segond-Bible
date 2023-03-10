package org.redesnac.lsgbible.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.redesnac.lsgbible.domain.model.verse.Verse

@Composable
fun VerseItem(
    verse: Verse
){
    SelectionContainer() {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "${verse.verse} ",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.primaryVariant
                )
                Text(
                    text = verse.text.replace("\u00b6", ""),
                    style = MaterialTheme.typography.h6
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}