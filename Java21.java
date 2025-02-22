///usr/bin/env jbang "$0" "$@" ; exit $?

//JAVA 21

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

public class Java21 {

    public static void main(String... args) {

        String dateString1 = "21 dic. 2024"; // With period
        String dateString2 = "25 dic 2024"; // Without period

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("dd MMM")
                .optionalStart()
                .appendLiteral(".")
                .optionalEnd()
                .appendPattern(" yyyy")
                .toFormatter(new Locale("es", "CO"));

        try {
            LocalDate date1 = LocalDate.parse(dateString1, formatter);
            System.out.println("Date 1: " + date1);

            LocalDate date2 = LocalDate.parse(dateString2, formatter);
            System.out.println("Date 2: " + date2);

            String formattedDate = date1.format(formatter);
            System.out.println("Formatted Date: " + formattedDate);

        } catch (DateTimeParseException e) {
            System.out.println("Error parsing date: " + e.getMessage());
        }

    }
}
