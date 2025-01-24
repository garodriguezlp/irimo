package com.github.garodriguezlp.irimo.formatter;

import com.github.garodriguezlp.irimo.domain.FinancialRecord;
import com.github.garodriguezlp.irimo.domain.FormattedFinancialRecord;
import java.util.List;

public interface FinancialRecordFormatter {

  FormattedFinancialRecord format(FinancialRecord record);

  default List<FormattedFinancialRecord> format(List<FinancialRecord> records) {
    return records.stream()
        .map(this::format)
        .toList();
  }

}
