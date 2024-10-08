package com.garodriguezlp.irimo.formatter;

import com.garodriguezlp.irimo.domain.FinancialRecord;
import com.garodriguezlp.irimo.domain.FormattedFinancialRecord;
import java.util.List;

public interface FinancialRecordFormatter {

  FormattedFinancialRecord format(FinancialRecord record);

  default List<FormattedFinancialRecord> format(List<FinancialRecord> records) {
    return records.stream()
        .map(this::format)
        .toList();
  }

}
