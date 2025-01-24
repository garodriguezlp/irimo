package com.github.garodriguezlp.irimo.formatter;

import com.github.garodriguezlp.irimo.domain.FinancialRecord;
import com.github.garodriguezlp.irimo.domain.FormattedFinancialRecord;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class DefaultRecordFormatterImpl implements
    FinancialRecordFormatter {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
  private static final NumberFormat CURRENCY_INSTANCE = NumberFormat.getCurrencyInstance(Locale.US);

  @Override
  public FormattedFinancialRecord format(FinancialRecord record) {
    return new FormattedFinancialRecord(
        formatDate(record.date()),
        record.description(),
        formatAmount(record.amount()),
        record.source());
  }

  private String formatAmount(Number amount) {
    return CURRENCY_INSTANCE.format(amount);
  }

  private String formatDate(LocalDate date) {
    return date.format(FORMATTER);
  }
}
