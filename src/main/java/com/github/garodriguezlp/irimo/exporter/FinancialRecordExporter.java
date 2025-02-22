package com.github.garodriguezlp.irimo.exporter;

import com.github.garodriguezlp.irimo.domain.FormattedFinancialRecord;
import com.github.garodriguezlp.irimo.service.Identifiable;
import java.util.List;

// @todo: Future implementations of this interface will combine a writer (e.g., CSVWriter) with an OutputStream.
//        Consider using Writer, OutputStream, or Appendable types, and take inspiration from Jackson to create a flexible and maintainable solution.
public interface FinancialRecordExporter extends Identifiable {

  void export(List<FormattedFinancialRecord> financialRecords);

}
