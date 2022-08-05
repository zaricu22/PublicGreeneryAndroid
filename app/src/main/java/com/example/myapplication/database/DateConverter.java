package com.example.myapplication.database;

import androidx.room.TypeConverter;
import java.util.Date;

// Room baza ne podrzava direktano skladistenje objekata vec mora preko konvertora
// Skladistimo Date kao Long u/iz baze
public class DateConverter {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
