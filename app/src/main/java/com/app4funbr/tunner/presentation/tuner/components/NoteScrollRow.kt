package com.app4funbr.tunner.presentation.tuner.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app4funbr.tunner.domain.model.MusicalNote

/**
 * Scroll horizontal de notas musicais.
 */
@Composable
fun NoteScrollRow(
    notes: List<MusicalNote>,
    currentNote: String,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val currentIndex: Int = notes.indexOfFirst { it.name == currentNote }
    LaunchedEffect(currentIndex) {
        if (currentIndex >= 0) {
            listState.animateScrollToItem(
                index = maxOf(0, currentIndex - 2),
                scrollOffset = 0
            )
        }
    }
    LazyRow(
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier
    ) {
        items(notes) { note ->
            NoteCard(
                noteName = note.name,
                isSelected = note.name == currentNote
            )
        }
    }
}

@Composable
private fun NoteCard(
    noteName: String,
    isSelected: Boolean
) {
    val backgroundColor: Color = if (isSelected) {
        Color(0xFF065f46)
    } else {
        Color.Transparent
    }
    val borderColor: Color = if (isSelected) {
        Color(0xFF10B981)
    } else {
        Color.Gray.copy(alpha = 0.3f)
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = noteName,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0f172a)
@Composable
fun PreviewNoteScrollRow() {
    val previewNotes: List<MusicalNote> = MusicalNote.getAllNotes().take(20)
    NoteScrollRow(
        notes = previewNotes,
        currentNote = "A4"
    )
}

