package com.kodemukti.namaibayi.data.local.database

import androidx.room.TypeConverter
import java.util.UUID

class Converters {
    @TypeConverter
    fun fromUUID(uuid: UUID): String = uuid.toString()

    @TypeConverter
    fun toUUID(value: String): UUID = UUID.fromString(value)
}
