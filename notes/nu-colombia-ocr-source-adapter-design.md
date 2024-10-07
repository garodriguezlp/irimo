## Architecture Overview

To design an OCR source adapter for extracting financial records in a Spring Boot application while optimizing for **decoupling**,
**testability**, and **reusability**, you can structure your solution into distinct, interchangeable components. Here's a
high-level overview of the main abstractions and their interactions:

1. **Filter Pipeline**: Applies a series of image filters to preprocess the OCR images.
2. **OCR Processor**: Converts the filtered image into a text string using an OCR engine.
3. **Financial Record Extractor**: Parses the OCR output to extract structured financial records.

## Main Components and Abstractions

### 1. Filter Pipeline

**Purpose**: Preprocess images to enhance OCR accuracy by removing noise, cropping, etc.

**Design**:
- **Filter Interface**: Defines the contract for image filters.
- **Filter Pipeline**: Manages and applies a sequence of filters to an image.

```java
public interface ImageFilter {
    BufferedImage apply(BufferedImage image);
}

public class FilterPipeline {
    private final List<ImageFilter> filters;

    public FilterPipeline(List<ImageFilter> filters) {
        this.filters = filters;
    }

    public BufferedImage execute(BufferedImage image) {
        BufferedImage processedImage = image;
        for (ImageFilter filter : filters) {
            processedImage = filter.apply(processedImage);
        }
        return processedImage;
    }
}
```

### 2. OCR Processor

**Purpose**: Perform OCR on the filtered image to extract text.

**Design**:
- **OcrProcessor Interface**: Abstracts the OCR implementation.
- **TesseractOcrProcessor**: An example implementation using Tesseract.

```java
public interface OcrProcessor {
    String performOcr(BufferedImage image);
}

public class TesseractOcrProcessor implements OcrProcessor {
    @Override
    public String performOcr(BufferedImage image) {
        // Integration with Tesseract OCR library
        // Return extracted text
    }
}
```

### 3. Financial Record Extractor

**Purpose**: Parse the OCR text to extract financial records specific to each financial institution.

**Design**:
- **FinancialRecordExtractor Interface**: Defines the extraction logic.
- **InstitutionSpecificRecordExtractor**: Implements extraction logic for a particular institution.

```java
public interface FinancialRecordExtractor {
    List<FinancialRecord> extractRecords(String ocrText);
}

public class BankARecordExtractor implements FinancialRecordExtractor {
    @Override
    public List<FinancialRecord> extractRecords(String ocrText) {
        // Parsing logic tailored to Bank A's statement format
    }
}
```

### 4. DataExtractor Implementation

**Purpose**: Orchestrate the entire extraction process by utilizing the above components.

**Design**:
- **NuColombiaDataExtractor**: Implements `DataExtractor` interface using the filter pipeline, OCR processor, and record extractor.

```java
@Service
public class NuColombiaDataExtractor implements DataExtractor {

    private final FilterPipeline filterPipeline;
    private final OcrProcessor ocrProcessor;
    private final FinancialRecordExtractor recordExtractor;

    @Autowired
    public NuColombiaDataExtractor(FilterPipeline filterPipeline,
                                   OcrProcessor ocrProcessor,
                                   FinancialRecordExtractor recordExtractor) {
        this.filterPipeline = filterPipeline;
        this.ocrProcessor = ocrProcessor;
        this.recordExtractor = recordExtractor;
    }

    @Override
    public List<FinancialRecord> extract(File dataDirectory) {
        List<FinancialRecord> records = new ArrayList<>();
        for (File imageFile : Objects.requireNonNull(dataDirectory.listFiles())) {
            BufferedImage image = ImageIO.read(imageFile);
            BufferedImage filteredImage = filterPipeline.execute(image);
            String ocrText = ocrProcessor.performOcr(filteredImage);
            records.addAll(recordExtractor.extractRecords(ocrText));
        }
        return records;
    }
}
```

## Testing Strategy

Each component is designed to be **independently testable**:

- **Image Filters**: Mock input images and verify the output after applying each filter.
- **OCR Processor**: Mock the OCR engine to return predefined strings and test the integration.
- **Financial Record Extractor**: Provide sample OCR texts and assert the correctness of extracted records.
- **DataExtractor**: Use mocks for all dependencies to test the orchestration logic without relying on actual implementations.

## Dependency Injection and Configuration

Leverage Spring Boot's dependency injection to manage component dependencies, enhancing **decoupling** and **reusability**.

```java
@Configuration
public class AppConfig {

    @Bean
    public List<ImageFilter> imageFilters() {
        return Arrays.asList(new CropFilter(), new NoiseReductionFilter());
    }

    @Bean
    public FilterPipeline filterPipeline(List<ImageFilter> imageFilters) {
        return new FilterPipeline(imageFilters);
    }

    @Bean
    public OcrProcessor ocrProcessor() {
        return new TesseractOcrProcessor();
    }

    @Bean
    public FinancialRecordExtractor financialRecordExtractor() {
        return new BankARecordExtractor();
    }
}
```

## Summary

This design ensures that each component of the OCR extraction process is **modular**, **testable**, and **reusable**. By adhering
to SOLID principles and leveraging Spring Boot's capabilities, you can maintain a clean architecture that's adaptable to changes,
such as supporting new financial institutions or integrating different OCR engines.

## Refined Architecture Design

To accommodate the need for greater flexibility and specificity per financial institution, the architecture can be adjusted to separate reusable components from institution-specific logic. Here's the enhanced design:

### 1. Generic Data Extractor

**Purpose**: Serve as a centralized orchestrator for the extraction process, adaptable to various institutions.

**Design**:
- **GenericDataExtractor Interface**: Retains the `DataExtractor` contract with a more generic name.
- **DefaultDataExtractor**: Implements the interface and delegates tasks to filters, OCR processor, and record extractor based on configuration.

```java
public interface DataExtractor {
    List<FinancialRecord> extract(File dataDirectory);
}

@Service
public class DefaultDataExtractor implements DataExtractor {

    private final FilterPipelineFactory filterPipelineFactory;
    private final OcrProcessor ocrProcessor;
    private final FinancialRecordExtractorFactory recordExtractorFactory;

    @Autowired
    public DefaultDataExtractor(FilterPipelineFactory filterPipelineFactory,
                                OcrProcessor ocrProcessor,
                                FinancialRecordExtractorFactory recordExtractorFactory) {
        this.filterPipelineFactory = filterPipelineFactory;
        this.ocrProcessor = ocrProcessor;
        this.recordExtractorFactory = recordExtractorFactory;
    }

    @Override
    public List<FinancialRecord> extract(File dataDirectory) {
        List<FinancialRecord> records = new ArrayList<>();
        for (File imageFile : Objects.requireNonNull(dataDirectory.listFiles())) {
            BufferedImage image = ImageIO.read(imageFile);

            // Retrieve the appropriate filter pipeline based on institution
            FilterPipeline filterPipeline = filterPipelineFactory.getFilterPipeline(imageFile);
            BufferedImage filteredImage = filterPipeline != null ? filterPipeline.execute(image) : image;

            String ocrText = ocrProcessor.performOcr(filteredImage);

            // Retrieve the appropriate record extractor based on institution
            FinancialRecordExtractor recordExtractor = recordExtractorFactory.getRecordExtractor(imageFile);
            records.addAll(recordExtractor.extractRecords(ocrText));
        }
        return records;
    }
}
```

### 2. Filter Pipeline Factory

**Purpose**: Dynamically provide the appropriate `FilterPipeline` based on the source image or institution.

**Design**:
- **FilterPipelineFactory Interface**: Defines a method to obtain a `FilterPipeline`.
- **InstitutionFilterPipelineFactory**: Implements the factory to return pipelines specific to each institution.

```java
public interface FilterPipelineFactory {
    FilterPipeline getFilterPipeline(File imageFile);
}

@Service
public class InstitutionFilterPipelineFactory implements FilterPipelineFactory {

    private final Map<String, FilterPipeline> pipelines;

    @Autowired
    public InstitutionFilterPipelineFactory(List<FilterPipeline> filterPipelines) {
        this.pipelines = filterPipelines.stream()
                                        .collect(Collectors.toMap(FilterPipeline::getInstitution, Function.identity()));
    }

    @Override
    public FilterPipeline getFilterPipeline(File imageFile) {
        String institution = determineInstitution(imageFile);
        return pipelines.get(institution);
    }

    private String determineInstitution(File imageFile) {
        // Logic to determine the institution based on the file or metadata
    }
}
```

### 3. Financial Record Extractor Factory

**Purpose**: Provide the appropriate `FinancialRecordExtractor` tailored to each financial institution.

**Design**:
- **FinancialRecordExtractorFactory Interface**: Defines a method to obtain a `FinancialRecordExtractor`.
- **InstitutionFinancialRecordExtractorFactory**: Implements the factory to return extractors specific to each institution.

```java
public interface FinancialRecordExtractorFactory {
    FinancialRecordExtractor getRecordExtractor(File imageFile);
}

@Service
public class InstitutionFinancialRecordExtractorFactory implements FinancialRecordExtractorFactory {

    private final Map<String, FinancialRecordExtractor> extractors;

    @Autowired
    public InstitutionFinancialRecordExtractorFactory(List<FinancialRecordExtractor> extractorsList) {
        this.extractors = extractorsList.stream()
                                        .collect(Collectors.toMap(FinancialRecordExtractor::getInstitution, Function.identity()));
    }

    @Override
    public FinancialRecordExtractor getRecordExtractor(File imageFile) {
        String institution = determineInstitution(imageFile);
        return extractors.get(institution);
    }

    private String determineInstitution(File imageFile) {
        // Logic to determine the institution based on the file or metadata
    }
}
```

### 4. FilterPipeline Enhancements

**Purpose**: Associate each `FilterPipeline` with a specific financial institution.

**Design**:
- **FilterPipeline Class**: Includes an identifier for the institution it serves.

```java
public class FilterPipeline {
    private final String institution;
    private final List<ImageFilter> filters;

    public FilterPipeline(String institution, List<ImageFilter> filters) {
        this.institution = institution;
        this.filters = filters;
    }

    public String getInstitution() {
        return institution;
    }

    public BufferedImage execute(BufferedImage image) {
        BufferedImage processedImage = image;
        for (ImageFilter filter : filters) {
            processedImage = filter.apply(processedImage);
        }
        return processedImage;
    }
}
```

### 5. ImageFilter Management

**Purpose**: Manage multiple `ImageFilter` implementations and associate them with respective `FilterPipeline`.

**Design**:
- **ImageFilter Beans**: Define each filter as a Spring bean.
- **FilterPipeline Beans**: Define each pipeline with a specific set of filters.

```java
@Configuration
public class FilterConfig {

    @Bean
    public ImageFilter cropFilter() {
        return new CropFilter();
    }

    @Bean
    public ImageFilter noiseReductionFilter() {
        return new NoiseReductionFilter();
    }

    @Bean
    public FilterPipeline bankAFilterPipeline() {
        return new FilterPipeline("BankA", Arrays.asList(cropFilter(), noiseReductionFilter()));
    }

    @Bean
    public FilterPipeline bankBFilterPipeline() {
        return new FilterPipeline("BankB", Arrays.asList(cropFilter()));
    }

    // Add more pipelines as needed
}
```

## Proposed Package Structure

Organizing the project into clear packages enhances maintainability and scalability. Here's a suggested structure:

```
src/main/java/com/yourcompany/ocradapter/
|
|-- common/
|   |-- filter/
|   |   |-- ImageFilter.java
|   |   |-- CropFilter.java
|   |   |-- NoiseReductionFilter.java
|   |   |-- ...
|   |-- ocr/
|   |   |-- OcrProcessor.java
|   |   |-- TesseractOcrProcessor.java
|   |   |-- ...
|   |-- pipeline/
|       |-- FilterPipeline.java
|       |-- FilterPipelineFactory.java
|       |-- InstitutionFilterPipelineFactory.java
|
|-- extractors/
|   |-- FinancialRecordExtractor.java
|   |-- BankARecordExtractor.java
|   |-- BankBRecordExtractor.java
|   |-- ...
|   |-- factory/
|       |-- FinancialRecordExtractorFactory.java
|       |-- InstitutionFinancialRecordExtractorFactory.java
|
|-- config/
|   |-- AppConfig.java
|   |-- FilterConfig.java
|
|-- service/
|   |-- DataExtractor.java
|   |-- DefaultDataExtractor.java
|
|-- model/
|   |-- FinancialRecord.java
|
|-- utils/
    |-- InstitutionDeterminationUtil.java
```

### **Package Breakdown**

- **common**: Contains reusable components like image filters, OCR processors, and filter pipelines.
  - **filter**: All image filter implementations.
  - **ocr**: OCR processing interfaces and implementations.
  - **pipeline**: Filter pipeline and its factories.

- **extractors**: Houses financial record extractors specific to each institution and their corresponding factories.
  - **factory**: Factories to retrieve the correct extractor based on the institution.

- **config**: Configuration classes for Spring beans, including filter pipelines and other common components.

- **service**: Core services like the generic `DataExtractor`.

- **model**: Domain models such as `FinancialRecord`.

- **utils**: Utility classes, e.g., for determining the institution from an image file.

## Managing FilterPipeline Initialization with Spring

To dynamically create and configure `FilterPipeline` instances based on available `ImageFilter` beans, leveraging Spring's dependency injection is effective. Here's how to approach it:

### 1. Define ImageFilter Beans

Each `ImageFilter` is defined as a Spring bean, allowing Spring to manage their lifecycle and dependencies.

```java
@Configuration
public class FilterConfig {

    @Bean
    public ImageFilter cropFilter() {
        return new CropFilter();
    }

    @Bean
    public ImageFilter noiseReductionFilter() {
        return new NoiseReductionFilter();
    }

    // Additional filters can be defined here
}
```

### 2. Configure FilterPipelines per Institution

Each `FilterPipeline` is associated with a specific institution and composed of relevant `ImageFilter` beans.

```java
@Configuration
public class PipelineConfig {

    @Autowired
    private ImageFilter cropFilter;

    @Autowired
    private ImageFilter noiseReductionFilter;

    @Bean
    public FilterPipeline bankAFilterPipeline() {
        return new FilterPipeline("BankA", Arrays.asList(cropFilter, noiseReductionFilter));
    }

    @Bean
    public FilterPipeline bankBFilterPipeline() {
        return new FilterPipeline("BankB", Collections.singletonList(cropFilter));
    }

    // Define more pipelines as needed
}
```

### 3. Assemble FilterPipelineFactory

The `FilterPipelineFactory` collects all `FilterPipeline` beans and provides the appropriate pipeline based on the institution.

```java
@Service
public class InstitutionFilterPipelineFactory implements FilterPipelineFactory {

    private final Map<String, FilterPipeline> pipelines;

    @Autowired
    public InstitutionFilterPipelineFactory(List<FilterPipeline> filterPipelines) {
        this.pipelines = filterPipelines.stream()
                                        .collect(Collectors.toMap(FilterPipeline::getInstitution, Function.identity()));
    }

    @Override
    public FilterPipeline getFilterPipeline(File imageFile) {
        String institution = determineInstitution(imageFile);
        return pipelines.get(institution);
    }

    private String determineInstitution(File imageFile) {
        // Implement logic to determine the institution from the image file
    }
}
```

### 4. Leveraging Spring Profiles (Optional)

If different environments require distinct filter pipelines, Spring Profiles can be utilized to activate specific configurations.

```java
@Configuration
@Profile("bankA")
public class BankAPipelineConfig {

    @Bean
    public FilterPipeline bankAFilterPipeline(ImageFilter cropFilter, ImageFilter noiseReductionFilter) {
        return new FilterPipeline("BankA", Arrays.asList(cropFilter, noiseReductionFilter));
    }
}

@Configuration
@Profile("bankB")
public class BankBPipelineConfig {

    @Bean
    public FilterPipeline bankBFilterPipeline(ImageFilter cropFilter) {
        return new FilterPipeline("BankB", Collections.singletonList(cropFilter));
    }
}
```

## Summary

The refined design emphasizes **flexibility** and **scalability** by separating reusable components from institution-specific logic. The use of factories for both filter pipelines and financial record extractors ensures that the system can easily adapt to various financial institutions without extensive modifications.

### **Key Enhancements:**

- **Generic Naming**: Renamed `NuColombiaDataExtractor` to `DefaultDataExtractor` for broader applicability.

- **Factory Pattern**: Introduced factories for `FilterPipeline` and `FinancialRecordExtractor` to manage institution-specific configurations dynamically.

- **Package Structure**: Organized the project into `common`, `extractors`, `config`, `service`, `model`, and `utils` packages to clearly delineate reusable components from specialized logic.

- **Spring Integration**: Leveraged Spring's dependency injection to manage `ImageFilter` beans and configure `FilterPipeline` instances per institution, promoting maintainability and ease of testing.

By following this architecture, you ensure that the OCR adapter remains **modular**, **extensible**, and **maintainable**, allowing for seamless integration of new financial institutions and adaptation to evolving requirements.
