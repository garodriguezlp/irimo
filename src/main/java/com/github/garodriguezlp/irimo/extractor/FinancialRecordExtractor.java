package com.github.garodriguezlp.irimo.extractor;

import com.github.garodriguezlp.irimo.domain.FinancialRecord;
import com.github.garodriguezlp.irimo.service.Identifiable;
import java.io.File;
import java.util.List;

public interface FinancialRecordExtractor extends Identifiable {

  List<FinancialRecord> extract(List<File> recordSources);

}
