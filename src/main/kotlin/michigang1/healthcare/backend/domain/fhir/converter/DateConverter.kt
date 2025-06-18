package michigang1.healthcare.backend.common.util.fhir

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

object DateConverter {
    fun parseDate(dateString: String): Date? {
        return try {
            val localDate = LocalDate.parse(dateString)
            convertLocalDateToDate(localDate)
        } catch (e: Exception) {
            null
        }
    }

    fun convertLocalDateToDate(localDate: LocalDate): Date {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
    }

    fun convertLocalDateTimeToDate(localDateTime: LocalDateTime): Date {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
    }

    fun convertDateToLocalDate(date: Date): LocalDate {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    fun convertDateToLocalDateTime(date: Date): LocalDateTime {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    }
}