package sample.gthio.tasks.data.model

import kotlinx.datetime.Instant

class Timestamp private constructor(
    val seconds: Long,
    val nanoseconds: Int
) {
    fun toInstant(): Instant = Instant.fromEpochSeconds(
        epochSeconds = seconds,
        nanosecondAdjustment = nanoseconds
    )

    companion object {
        fun fromInstant(instant: Instant): Timestamp = Timestamp(
            seconds = instant.epochSeconds,
            nanoseconds = instant.nanosecondsOfSecond,
        )
    }
}