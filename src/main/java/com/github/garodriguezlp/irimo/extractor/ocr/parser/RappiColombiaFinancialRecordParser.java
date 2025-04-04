package com.github.garodriguezlp.irimo.extractor.ocr.parser;

import static com.github.garodriguezlp.irimo.extractor.ocr.parser.util.TextChunkSplitter.splitIntoChunksByEmptyLines;

import com.github.garodriguezlp.irimo.domain.FinancialRecord;
import com.github.garodriguezlp.irimo.extractor.ocr.exception.FinancialRecordParsingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
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
  private static final DateTimeFormatter DATE_FORMATTER = buildCaseInsensitiveUsFormatter();

  @Override
  public List<FinancialRecord> extractRecords(String input) {
    try {
      LOGGER.debug("Parsing {} OCR data", SOURCE);
      LOGGER.trace("Raw OCR data: {}", input);
      List<List<String>> rawRecords = splitIntoChunksByEmptyLines(input);
      return rawRecords.stream()
          .map(RappiColombiaFinancialRecordParser::createFinancialRecord)
          .peek(record -> LOGGER.trace("Parsed financial record: {}", record))
          .flatMap(Optional::stream)
          .toList();
    } catch (Exception e) {
      throw new FinancialRecordParsingException("Failed to parse financial records from OCR data",
          e);
    }
  }

  private static Optional<FinancialRecord> createFinancialRecord(List<String> rawRecord) {
    String joinedRecord = String.join(" ", rawRecord);
    String description = extractDescription(joinedRecord);
    BigDecimal amount = extractAmount(joinedRecord);
    LocalDate date = extractDate(joinedRecord);

    if (description.isEmpty() || amount.equals(BigDecimal.ZERO) || date == null) {
      LOGGER.warn("Failed to parse financial record: {}", joinedRecord);
      return Optional.empty();
    }

    return Optional.of(new FinancialRecord(date, description, amount, SOURCE));
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
      return "-".equals(sign) ? amount : amount.negate();
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

  static DateTimeFormatter buildCaseInsensitiveUsFormatter() {
    return new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendPattern("d MMM yyyy")
        .toFormatter(Locale.US);
  }

}
