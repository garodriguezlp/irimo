package com.garodriguezlp.irimo.extractor.ocr;

import static java.util.Collections.emptyList;

import com.garodriguezlp.irimo.domain.FinancialRecord;
import com.garodriguezlp.irimo.extractor.FinancialRecordExtractor;
import com.garodriguezlp.irimo.extractor.ocr.exception.ImageFilteringException;
import com.garodriguezlp.irimo.extractor.ocr.exception.FinancialRecordParsingException;
import com.garodriguezlp.irimo.extractor.ocr.exception.OcrProcessingException;
import com.garodriguezlp.irimo.extractor.ocr.filter.ImageFilterPipeline;
import com.garodriguezlp.irimo.extractor.ocr.parser.FinancialRecordParser;
import com.garodriguezlp.irimo.extractor.ocr.processor.OcrProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class OcrFinancialRecordExtractor implements FinancialRecordExtractor {

  private static final Logger logger = LoggerFactory.getLogger(OcrFinancialRecordExtractor.class);

  private final ImageFilterPipeline imageFilterPipeline;
  private final OcrProcessor ocrProcessor;
  private final FinancialRecordParser financialRecordParser;

  public OcrFinancialRecordExtractor(
      ImageFilterPipeline imageFilterPipeline,
      OcrProcessor ocrProcessor,
      FinancialRecordParser financialRecordParser) {
    this.imageFilterPipeline = imageFilterPipeline;
    this.ocrProcessor = ocrProcessor;
    this.financialRecordParser = financialRecordParser;
  }

  @Override
  public List<FinancialRecord> extract(File dataDirectory) {
    logger.info("Starting extraction from directory: {}", dataDirectory.getAbsolutePath());

    return Stream.of(Objects.requireNonNull(dataDirectory.listFiles()))
        .filter(File::isFile)
        .map(this::processImageAndExtractRecords)
        .flatMap(List::stream)
        .toList();
  }

  private List<FinancialRecord> processImageAndExtractRecords(File imageFile) {
    logger.info("Processing image file: {}", imageFile.getName());

    try {
      BufferedImage image = ImageIO.read(imageFile);
      BufferedImage filteredImage = imageFilterPipeline.execute(image);
      String ocrText = ocrProcessor.performOcr(filteredImage);
      List<FinancialRecord> records = financialRecordParser.extractRecords(ocrText);

      logger.info("Successfully processed {} and extracted {} records",
          imageFile.getName(), records.size());
      return records;
    } catch (IOException |
             ImageFilteringException |
             OcrProcessingException |
             FinancialRecordParsingException e) {
      logger.error("Error processing file {}: {}", imageFile.getName(), e.getMessage(), e);
      return emptyList();
    } catch (Exception e) {
      logger.error("Unexpected error while processing image file: {}", imageFile.getName(), e);
      return emptyList();
    }
  }
}
