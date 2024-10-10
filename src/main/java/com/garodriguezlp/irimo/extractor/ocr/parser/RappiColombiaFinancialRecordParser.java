package com.garodriguezlp.irimo.extractor.ocr.parser;

import com.garodriguezlp.irimo.domain.FinancialRecord;
import com.garodriguezlp.irimo.extractor.ocr.exception.FinancialRecordParsingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RappiColombiaFinancialRecordParser implements FinancialRecordParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(
      RappiColombiaFinancialRecordParser.class);

  private static final String SOURCE = "Rappi";

  private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("^(.*?)\\s*[+-]?\\$");
  private static final Pattern AMOUNT_PATTERN = Pattern.compile("([+-]?)\\$([\\d.,]+)");
  private static final Pattern DATE_PATTERN = Pattern.compile(
      "(\\d{1,2}\\s+[A-Za-z]{3}\\s+\\d{4})");
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d MMM yyyy");

  @Override
  public List<FinancialRecord> extractRecords(String input) {
    try {
      LOGGER.debug("Parsing {} OCR data", SOURCE);
      List<List<String>> rawRecords = breakIntoChunks(input);
      return rawRecords.stream()
          .map(RappiColombiaFinancialRecordParser::createFinancialRecord)
          .toList();
    } catch (Exception e) {
      throw new FinancialRecordParsingException("Failed to parse financial records from OCR data",
          e);
    }
  }

  private static FinancialRecord createFinancialRecord(List<String> rawRecord) {
    String joinedRecord = String.join(" ", rawRecord);
    String description = extractDescription(joinedRecord);
    BigDecimal amount = extractAmount(joinedRecord);
    LocalDate date = extractDate(joinedRecord);
    return new FinancialRecord(date, description, amount, SOURCE);
  }

  public static String extractDescription(String rawRecord) {
    Matcher matcher = DESCRIPTION_PATTERN.matcher(rawRecord);
    if (matcher.find()) {
      return matcher.group(1).trim();
    }
    return "";
  }

  public static BigDecimal extractAmount(String rawRecord) {
    Matcher matcher = AMOUNT_PATTERN.matcher(rawRecord);
    if (matcher.find()) {
      String sign = matcher.group(1);
      String amountStr = matcher.group(2).replace(".", "").replace(",", ".");
      BigDecimal amount = new BigDecimal(amountStr);
      return "-".equals(sign) ? amount.negate() : amount;
    }
    return BigDecimal.ZERO;
  }

  public static LocalDate extractDate(String rawRecord) {
    Matcher matcher = DATE_PATTERN.matcher(rawRecord);
    if (matcher.find()) {
      return LocalDate.parse(matcher.group(1), DATE_FORMATTER);
    }
    return null;
  }

  public static List<List<String>> breakIntoChunks(String input) {
    String[] lines = input.trim().split("\n");
    List<List<String>> rawRecords = new ArrayList<>();
    List<String> currentBucket = new ArrayList<>();
    rawRecords.add(currentBucket);

    for (int i = 0; i < lines.length; i++) {
      String line = lines[i].trim();
      currentBucket.add(line);

      if (isDateLine(line)) {
        boolean isThereAnotherLine = i + 1 < lines.length;
        if (isThereAnotherLine && (containsAmount(lines[i + 1]) || lines[i + 1].contains(
            "Pending"))) {
          currentBucket.add(lines[i + 1].trim());
          // @todo: improve
          i++;
        }
        currentBucket = new ArrayList<>();
        rawRecords.add(currentBucket);
      }
    }

    // Remove any empty buckets
    rawRecords.removeIf(List::isEmpty);

    return rawRecords;
  }

  private static boolean isDateLine(String line) {
    // Simple date pattern: day month year
    return Pattern.compile("\\d{1,2}\\s+[A-Za-z]+\\s+\\d{4}").matcher(line).find();
  }

  private static boolean containsAmount(String line) {
    // Check if the line starts with a monetary amount
    // It can optionally start with a plus or minus sign
    // The amount should be the only content on the line (excluding whitespace)
    // Allow for thousands separators and decimal points
    return Pattern.compile("^\\s*[+-]?\\s*\\$?\\d{1,3}([.,]\\d{3})*([.,]\\d{2})?\\s*$")
        .matcher(line).find();
  }
}
