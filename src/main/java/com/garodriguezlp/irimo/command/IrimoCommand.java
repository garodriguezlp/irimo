package com.garodriguezlp.irimo.command;

import com.garodriguezlp.irimo.service.FinancialRecordsConsolidationService;
import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Component
@Command(name = "irimo", description = "Process and export financial records")
public class IrimoCommand implements Callable<Integer> {

  private final FinancialRecordsConsolidationService service;

  @Option(
      names = {"-s", "--source"},
      description = "Source identifier for the financial records",
      required = true)
  private String source;

  @Option(
      names = {"-t", "--target"},
      description = "Target identifier for exporting the records",
      required = true,
      defaultValue = "clipboard")
  private String target;

  @Parameters(index = "0..*", description = "Input files to process")
  private List<File> files;

  public IrimoCommand(FinancialRecordsConsolidationService service) {
    this.service = service;
  }

  @Override
  public Integer call() throws Exception {
    service.processRecords(files, source, target);
    return 0;
  }
}
