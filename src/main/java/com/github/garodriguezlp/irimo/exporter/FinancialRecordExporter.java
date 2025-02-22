package com.github.garodriguezlp.irimo.exporter;

import com.github.garodriguezlp.irimo.domain.FormattedFinancialRecord;
import com.github.garodriguezlp.irimo.service.Identifiable;
import java.util.List;

// @todo: Future implementations of this interface should integrate a custom writer capable of
//        writing a list of financial records (e.g., CSVWriter) with an appropriate output stream.
//        Consider utilizing Writer, OutputStream, or Appendable types, and draw inspiration from
//        libraries such as Jackson to create a flexible and maintainable solution.
public interface FinancialRecordExporter extends Identifiable {

  void export(List<FormattedFinancialRecord> financialRecords);

}
