package com.github.garodriguezlp.irimo;

import com.github.garodriguezlp.irimo.command.IrimoCommand;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

@SpringBootApplication
public class IrimoApplication implements CommandLineRunner, ExitCodeGenerator {

  private final IFactory factory;
  private final IrimoCommand irimoCommand;
  private int exitCode;

  public IrimoApplication(
      IFactory factory,
      IrimoCommand irimoCommand
  ) {
    this.factory = factory;
    this.irimoCommand = irimoCommand;
  }

  public static void main(String[] args) {
    System.exit(SpringApplication.exit(new SpringApplicationBuilder(IrimoApplication.class)
        .headless(false)
        .run(args)));
  }

  @Override
  public void run(String... args) throws Exception {
    exitCode = new CommandLine(irimoCommand, factory).execute(args);
  }

  @Override
  public int getExitCode() {
    return exitCode;
  }
}
