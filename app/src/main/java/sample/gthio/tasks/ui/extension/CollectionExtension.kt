package sample.gthio.tasks.ui.extension

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

internal fun <T> List<T>.addOrRemoveDuplicate(
    value: T,
    matcher: (T, T) -> Boolean,
): List<T> {
    return when (val index = this.indexOfFirst { v -> matcher(v, value) }) {
        -1 -> this + value
        else -> this.toMutableList().apply { removeAt(index) }
    }
}

fun <T> MutableStateFlow<T?>.updateNotNull(f: (T) -> T) {
    update { old ->
        when (old) {
            null -> null
            else -> f(old)
        }
    }
}