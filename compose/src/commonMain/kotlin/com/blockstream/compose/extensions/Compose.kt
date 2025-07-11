package com.blockstream.compose.extensions

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.min

fun MutableState<Boolean>.toggle() {
    this.value = !this.value
}

fun <T> MutableStateFlow<T>.onValueChange(maxChars: Int? = null): (T) -> Unit = { newValue ->
    if (maxChars != null && newValue is String) {
        (newValue as? String)?.also {
            @Suppress("UNCHECKED_CAST")
            this.value = newValue.substring(0, min(newValue.length, 1000)) as T
        }
    } else {
        this.value = newValue
    }
}

@Composable
fun MutableStateFlow<String>.onTextFieldValueChange(updateTextFieldValue: (textFieldValue: TextFieldValue) -> Unit): (TextFieldValue) -> Unit =
    { newValue ->
        updateTextFieldValue.invoke(newValue)
        this.value = newValue.text
    }

fun Color.Companion.random() = Color(
    kotlin.random.Random.nextInt(256),
    kotlin.random.Random.nextInt(256),
    kotlin.random.Random.nextInt(256)
)

fun Offset.toMenuDpOffset(container: IntSize = IntSize.Zero, density: Density): DpOffset =
    DpOffset(x = x.pxToDp(density), y = -(container.height - y).pxToDp(density))

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

@Composable
fun Float.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

fun Float.pxToDp(density: Density) = with(density) { this@pxToDp.toDp() }

inline fun <T> LazyListScope.itemsSpaced(
    items: List<T>,
    noinline key: ((index: Int, item: T) -> Any)? = null,
    space: Int = 8,
    crossinline contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) = itemsIndexed(items = items, key = key, contentType = contentType, itemContent = { index, item ->
    itemContent(item)
    if (index < items.size - 1) {
        Spacer(Modifier.height(space.dp))
    }
})