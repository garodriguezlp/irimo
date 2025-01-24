package com.github.garodriguezlp.irimo.extractor.ocr;

import static java.util.Collections.emptyList;

import com.github.garodriguezlp.irimo.domain.FinancialRecord;
import com.github.garodriguezlp.irimo.extractor.FinancialRecordExtractor;
import com.github.garodriguezlp.irimo.extractor.ocr.exception.FinancialRecordParsingException;
import com.github.garodriguezlp.irimo.extractor.ocr.exception.ImageFilteringException;
import com.github.garodriguezlp.irimo.extractor.ocr.exception.OcrProcessingException;
import com.github.garodriguezlp.irimo.extractor.ocr.filter.ImageFilterPipeline;
import com.github.garodriguezlp.irimo.extractor.ocr.parser.FinancialRecordParser;
import com.github.garodriguezlp.irimo.extractor.ocr.processor.OcrProcessor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OcrFinancialRecordExtractor implements FinancialRecordExtractor {

  private static final Logger LOGGER = LoggerFactory.getLogger(OcrFinancialRecordExtractor.class);

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
  public List<FinancialRecord> extract(List<File> recordSources) {
    LOGGER.debug("Starting extraction from {} source documents", recordSources.size());

    return recordSources.stream()
        .filter(File::isFile)
        .map(this::processImageAndExtractRecords)
        .flatMap(List::stream)
        .toList();
  }

  private List<FinancialRecord> processImageAndExtractRecords(File imageFile) {
    LOGGER.info("Processing image: {}", imageFile.getName());

    try {
      BufferedImage image = ImageIO.read(imageFile);
      BufferedImage filteredImage = imageFilterPipeline.execute(image);
      String ocrText = ocrProcessor.performOcr(filteredImage);
      List<FinancialRecord> records = financialRecordParser.extractRecords(ocrText);

      LOGGER.debug("Successfully processed {} and extracted {} records",
          imageFile.getName(), records.size());
      return records;
    } catch (IOException |
             ImageFilteringException |
             OcrProcessingException |
             FinancialRecordParsingException e) {
      LOGGER.error("Error processing file {}: {}", imageFile.getName(), e.getMessage(), e);
      return emptyList();
    } catch (Exception e) {
      LOGGER.error("Unexpected error while processing image file: {}", imageFile.getName(), e);
      return emptyList();
    }
  }
}
