package org.example.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for date-related operations.
 */
public class DateUtils {

    /**
     * Formats the given LocalDateTime to a string in the format day/month/year.
     *
     * @param date the LocalDateTime to format
     * @return the formatted date as a string in the format "dd/MM/yyyy"
     */
    public static String formatateDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }
}
