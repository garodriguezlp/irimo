# Design

## Vision and Intent

I'm looking to develop a CLI tool for data transformation of my financial records. To provide context, I utilize multiple
financial apps and aim to consolidate all of their data into a single source.

Some applications provide CSV files, while for others, I need to capture screenshots and extract data from the images.

The target format remains consistent across all sources.

My goal is to construct this tool in a way that allows for future evolution, supporting additional input sources and output
formats or destinations.

I'm seeking your assistance with the design, including high-level architecture and low-level details (such as concrete
abstractions: types, interfaces, entities, etc.) for this tool.

In this endeavor, I'd like to adopt, if feasible, hexagonal architecture and Domain-Driven Design (DDD) principles.

For me, a financial record consists of a date, a description, an amount, and the source of the record (e.g., bank name or
financial institution).

I'm considering modeling this around the concepts of inbound-adapter (OCR, CSV), transformer (or formatter, which I believe won't
likely change), and outbound-adapter (CSV, HTTP API, etc.).

This approach somewhat resembles an ETL (Extract, Transform, Load) process, though I'm not certain if it's the optimal way to
model this system.

My primary objectives are maintainability and extensibility. I want to be able to incorporate new input sources and output formats
with ease. Therefore, I'm aiming for a loosely coupled design.

## Designing a Maintainable and Extensible CLI Tool for Financial Data Transformation

## 2. High-Level Architecture Design

In this project, we will utilize Clean Architecture and Domain-Driven Design (DDD) to build a maintainable CLI tool for financial
record management.

The **Domain Layer** will encapsulate business logic, featuring entities like Transaction and Account, along with value objects
such as Currency and Date. Domain services like TransactionProcessor will handle complex operations.

The **Application Layer** will orchestrate use cases, defining ports for input, output, and storage. Key use cases include
ImportRecords, TransformData, and GenerateReport.

The **Adapters Layer** will connect our application to external systems through input adapters (CLI, file readers), output
adapters (console, file writers), and storage solutions (in-memory, database).

This structure promotes loose coupling, extensibility, and robust testability, enabling our CLI tool to evolve while maintaining
core functionality.

## 3. Domain Layer: Entities and Value Objects

### Entity: FinancialRecord

Represents a single financial transaction.

```java
public record FinancialRecord(
    LocalDate date,
    String description,
    BigDecimal amount,
    String source)
    { }

public record FormattedFinancialRecord(
    String formattedDate,
    String description,
    String formattedAmount,
    String source)
    { }
```

## 4. Application Layer: Use Cases and Services

### Use Case: Consolidate Financial Records

Manages the orchestration of data extraction, transformation, and loading.

```java
public class ConsolidateFinancialRecordsService {
    private final DataExtractor extractor;
    private final DataTransformer transformer;
    private final DataExtractor loader;

    public void execute() {
        List<RawData> rawData = extractor.extract();
        List<FinancialRecord> records = transformer.transform(rawData);
        loader.load(records);
    }
}
```

## 5. Adapters Layer: Inbound and Outbound Adapters

### Inbound Adapters (Data Extraction)

- **CSVExtractor**: Extracts data from CSV files.
- **OCExtractor**: Extracts data from images using OCR.

**Interface: DataExtractor**

```java
public interface DataExtractor {
    List<FinancialRecord> extract();
}
```

### Outbound Adapters (Data Loading)

- **CSVExtactor**: Writes transformed data to a CSV file.
- **APIExtactor**: Sends data to an HTTP API.

**Interface: DataExtractor**

```java
public interface DataExtractor {
    void load(List<FormattedFinancialRecord> records);
}
```

## 6. Data Transformation

### DataTransformer Interface

Encapsulates the logic for transforming financial records.

```java
public interface DataTransformer {
    List<FormattedFinancialRecord> transform(List<FinancialRecord> rawData);
}
```

## 7. CLI Interface

```java
@Command(name = "irimo", mixinStandardHelpOptions = true, version = "irimo 1.0",
         description = "Consolidates financial records from various sources.")
public class IrimoCommand implements Runnable {

    @Option(names = {"-s", "--source"}, description = "Source adapter (csv, ocr)")
    private String sourceAdapter = "csv";

    @Option(names = {"-e", "--export"}, description = "Export adapter (csv, api)")
    private String exportAdapter = "csv";

    @Parameters(description = "The files to process")
    private List<File> files;

    @Inject
    private ConsolidateFinancialRecordsService service;

    @Override
    public void run() {
        DataExtractor extractor = getExtractor(sourceAdapter);
        DataExporter exporter = getExporter(exportAdapter);
        DataTransformer transformer = new StandardTransformer();

        service = new ConsolidateFinancialRecordsService(extractor, transformer, exporter);
        service.execute(files);
    }

    private DataExtractor getExtractor(String type) {
        return switch (type) {
            case "csv" -> new CSVExtractor();
            case "ocr" -> new OCRExtractor();
            default -> throw new IllegalArgumentException("Unknown source adapter: " + type);
        };
    }

    private DataExporter getExporter(String type) {
        return switch (type) {
            case "csv" -> new CSVExporter();
            case "api" -> new APIExporter();
            default -> throw new IllegalArgumentException("Unknown export adapter: " + type);
        };
    }
}
```

## 8. Low level design

- [nu-colombia-ocr-source-adapter-design.md](nu-colombia-ocr-source-adapter-design.md)

## Improving OCR

- [tweaking-tess4j-for-better-results.md](tweaking-tess4j-for-better-results.md)
