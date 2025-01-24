package com.github.garodriguezlp.irimo.command;

import com.github.garodriguezlp.irimo.service.FinancialRecordsConsolidationService;
import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import org.springframework.stereotype.Component;
import picocli.AutoComplete.GenerateCompletion;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Component
@Command(
    name = "irimo",
    description = "Consolidates personal financial data for better decision-making.",
    mixinStandardHelpOptions = true,
    versionProvider = VersionProvider.class,
    subcommands = {
        GenerateCompletion.class
    }
)
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
