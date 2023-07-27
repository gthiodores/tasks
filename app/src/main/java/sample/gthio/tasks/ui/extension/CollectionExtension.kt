package sample.gthio.tasks.ui.extension

internal fun <T> List<T>.addOrRemoveDuplicate(
    value: T,
    matcher: (T, T) -> Boolean,
): List<T> {
    return when (val index = this.indexOfFirst { v -> matcher(v, value) }) {
        -1 -> this + value
        else -> this.toMutableList().apply { removeAt(index) }
    }
}