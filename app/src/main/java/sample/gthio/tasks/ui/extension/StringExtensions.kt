package sample.gthio.tasks.ui.extension

fun <T> StringBuilder.letAppend(
    value: T?,
    f: StringBuilder.(T) -> StringBuilder,
): StringBuilder {
    return when (value) {
        null -> this
        else -> f(value)
    }
}