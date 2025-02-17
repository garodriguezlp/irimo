package com.github.garodriguezlp.irimo.extractor.ocr.parser;

import com.github.garodriguezlp.irimo.domain.FinancialRecord;
import com.github.garodriguezlp.irimo.extractor.ocr.exception.FinancialRecordParsingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NuColombiaFinancialRecordParser implements FinancialRecordParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(
      NuColombiaFinancialRecordParser.class);

  private static final String SOURCE = "Nu";
  private static final Pattern AMOUNT_PATTERN = Pattern.compile("([+-]\\$[\\d.,]+)");
  private static final Pattern DATE_PATTERN = Pattern.compile(
      "(\\d{2} [a-z]{3}) - (\\d{2}:\\d{2})");
  private static final String ES_CO_SHORT_MONTH_SUFFIX = ".";
  // private static final Locale COLOMBIA_LOCALE = Locale.forLanguageTag("es-CO");
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("es", "CO"));
  private static final String UNWANTED_CHARS_REGEX = "[^a-zA-Z0-9\\s]";
  private static final String MULTIPLE_WHITESPACE_REGEX = "\\s+";

  @Override
  public List<FinancialRecord> extractRecords(String input) {
    try {
      LOGGER.debug("Parsing {} OCR data", SOURCE);
      List<List<String>> rawRecords = breakIntoChunks(input);
      return rawRecords.stream()
          .map(NuColombiaFinancialRecordParser::createFinancialRecord)
          .peek(record -> LOGGER.trace("Parsed financial record: {}", record))
          .flatMap(Optional::stream)
          .toList();
    } catch (Exception e) {
      throw new FinancialRecordParsingException("Failed to parse financial records from OCR data", e);
    }
  }

  // @todo: remove duplicate code
  public static List<List<String>> breakIntoChunks(String input) {
    String[] lines = input.trim().split("\n");
    List<List<String>> rawRecords = new ArrayList<>();
    List<String> currentBucket = new ArrayList<>();
    rawRecords.add(currentBucket);

    for (String line : lines) {
      line = line.trim();

      if (line.isEmpty()) {
        currentBucket = new ArrayList<>();
        rawRecords.add(currentBucket);
      } else {
        currentBucket.add(line);
      }
    }

    rawRecords.removeIf(List::isEmpty);

    return rawRecords;
  }

  private static Optional<FinancialRecord> createFinancialRecord(List<String> rawRecord) {
    String joinedRecord = String.join(" ", rawRecord);
    BigDecimal amount = extractAmount(joinedRecord);
    LocalDate date = extractDate(joinedRecord);
    String description = extractDescription(joinedRecord);

    if (description.isEmpty() || amount.equals(BigDecimal.ZERO) || date == null) {
      LOGGER.warn("Failed to parse financial record: {}", joinedRecord);
      return Optional.empty();
    }

    return Optional.of(new FinancialRecord(date, description, amount, SOURCE));
  }

  private static BigDecimal extractAmount(String rawRecord) {
    Matcher matcher = AMOUNT_PATTERN.matcher(rawRecord);
    if (matcher.find()) {
      String amountStr = matcher.group(1).replace("$", "").replace(".", "").replace(",", ".");
      return new BigDecimal(amountStr);
    }
    return BigDecimal.ZERO;
  }

  private static LocalDate extractDate(String rawRecord) {
    Matcher matcher = DATE_PATTERN.matcher(rawRecord);
    if (matcher.find()) {
      String dateStr =
          matcher.group(1) + ES_CO_SHORT_MONTH_SUFFIX + " " + LocalDate.now().getYear();
      return LocalDate.parse(dateStr, DATE_FORMATTER);
    }
    return null;
  }

  private static String extractDescription(String rawRecord) {
    Matcher amountMatcher = AMOUNT_PATTERN.matcher(rawRecord);
    String descriptionWithoutAmount = amountMatcher.replaceAll("");

    Matcher dateMatcher = DATE_PATTERN.matcher(descriptionWithoutAmount);
    String descriptionWithoutDateAndAmount = dateMatcher.replaceAll("");

    return descriptionWithoutDateAndAmount
        .replaceAll(UNWANTED_CHARS_REGEX, "")
        .replaceAll(MULTIPLE_WHITESPACE_REGEX, " ")
        .trim();
  }

}
