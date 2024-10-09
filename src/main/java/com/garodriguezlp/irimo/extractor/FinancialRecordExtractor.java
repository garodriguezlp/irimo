package com.garodriguezlp.irimo.extractor;

import com.garodriguezlp.irimo.domain.FinancialRecord;
import com.garodriguezlp.irimo.service.Identifiable;
import java.io.File;
import java.util.List;

public interface FinancialRecordExtractor extends Identifiable {

  List<FinancialRecord> extract(List<File> recordSources);

}
