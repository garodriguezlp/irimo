package com.garodriguezlp.irimo.extractor.ocr.parser;

import com.garodriguezlp.irimo.domain.FinancialRecord;
import com.garodriguezlp.irimo.extractor.ocr.exception.FinancialRecordParsingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NuColombiaFinancialRecordParser implements FinancialRecordParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(
      NuColombiaFinancialRecordParser.class);

  private static final String SOURCE = "Nu Colombia";
  private static final Pattern AMOUNT_PATTERN = Pattern.compile("([+-]\\$[\\d.,]+)");
  private static final Pattern DATE_PATTERN = Pattern.compile(
      "(\\d{2} [a-z]{3}) - (\\d{2}:\\d{2})");
  private static final String ES_CO_SHORT_MONTH_SUFFIX = ".";
  private static final Locale COLOMBIA_LOCALE = Locale.forLanguageTag("es-CO");
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy",
      COLOMBIA_LOCALE);
  private static final String UNWANTED_CHARS_REGEX = "[^a-zA-Z0-9\\s]";
  private static final String MULTIPLE_WHITESPACE_REGEX = "\\s+";

  @Override
  public List<FinancialRecord> extractRecords(String input) {
    try {
      LOGGER.info("Parsing {} OCR data", SOURCE);
      String sanitizedInput = sanitizeInput(input);
      List<List<String>> rawRecords = breakIntoChunks(sanitizedInput);
      return rawRecords.stream()
          .map(NuColombiaFinancialRecordParser::createFinancialRecord)
          .filter(this::isComplete)
          .toList();
    } catch (Exception e) {
      throw new FinancialRecordParsingException("Failed to parse financial records from OCR data", e);
    }
  }

  private static String sanitizeInput(String input) {
    String[] lines = input.split("\n");
    for (int currentRow = 0; currentRow < lines.length; currentRow++) {
      if (lines[currentRow].contains("$")) {
        return String.join("\n", Arrays.copyOfRange(lines, currentRow, lines.length));
      }
    }
    return "";
  }

  private static List<List<String>> breakIntoChunks(String sanitizedInput) {
    List<List<String>> rawRecords = new ArrayList<>();
    List<String> currentRecord = new ArrayList<>();

    for (String line : sanitizedInput.split("\n")) {
      if (line.contains("$")) {
        currentRecord = new ArrayList<>();
        rawRecords.add(currentRecord);
        currentRecord.add(line.trim());
      } else {
        currentRecord.add(line.trim());
      }
    }

    return rawRecords;
  }

  private static FinancialRecord createFinancialRecord(List<String> rawRecord) {
    BigDecimal amount = extractAmount(rawRecord);
    LocalDate date = extractDate(rawRecord);
    String description = extractDescription(rawRecord);
    return new FinancialRecord(date, description, amount, SOURCE);
  }

  private static BigDecimal extractAmount(List<String> rawRecord) {
    String amountLine = rawRecord.get(0);
    Matcher matcher = AMOUNT_PATTERN.matcher(amountLine);
    if (matcher.find()) {
      String amountStr = matcher.group(1).replace("$", "").replace(".", "").replace(",", ".");
      return new BigDecimal(amountStr);
    }
    return BigDecimal.ZERO;
  }

  private static LocalDate extractDate(List<String> rawRecord) {
    String dateLine = rawRecord.get(rawRecord.size() - 1);
    Matcher matcher = DATE_PATTERN.matcher(dateLine);
    if (matcher.find()) {
      String dateStr =
          matcher.group(1) + ES_CO_SHORT_MONTH_SUFFIX + " " + LocalDate.now().getYear();
      return LocalDate.parse(dateStr, DATE_FORMATTER);
    }
    return null;
  }

  private static String extractDescription(List<String> rawRecord) {
    int descriptionEndIndex = rawRecord.size() == 1 ? 1 : rawRecord.size() - 1;
    List<String> descriptionLines = rawRecord.subList(0, descriptionEndIndex);

    String joinedDescription = String.join(" ", descriptionLines);

    Matcher matcher = AMOUNT_PATTERN.matcher(joinedDescription);
    String descriptionWithoutAmount = matcher.replaceAll("");

    return descriptionWithoutAmount
        .replaceAll(UNWANTED_CHARS_REGEX, "")
        .replaceAll(MULTIPLE_WHITESPACE_REGEX, " ")
        .trim();
  }

  private boolean isComplete(FinancialRecord financialRecord) {
    if (financialRecord.date() == null) {
      LOGGER.warn("Record dropped due to missing date: {}", financialRecord);
      return false;
    }
    if (financialRecord.description() == null || financialRecord.description().isEmpty()) {
      LOGGER.warn("Record dropped due to missing description: {}", financialRecord);
      return false;
    }
    if (financialRecord.amount() == null) {
      LOGGER.warn("Record dropped due to missing amount: {}", financialRecord);
      return false;
    }
    return true;
  }
}
