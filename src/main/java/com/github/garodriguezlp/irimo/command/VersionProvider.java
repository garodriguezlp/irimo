package com.github.garodriguezlp.irimo.command;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import picocli.CommandLine.IVersionProvider;

@Component
public class VersionProvider implements IVersionProvider {

  private final String version;

  public VersionProvider(
      @Value("${application.version:0.0.1}")
      String version
  ) {
    this.version = version;
  }

  @Override
  public String[] getVersion() {
    return new String[]{"${COMMAND-FULL-NAME} version " + version};
  }
}
