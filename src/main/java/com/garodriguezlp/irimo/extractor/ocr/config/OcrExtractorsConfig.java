package com.garodriguezlp.irimo.extractor.ocr.config;

import com.garodriguezlp.irimo.extractor.ocr.OcrFinancialRecordExtractor;
import com.garodriguezlp.irimo.extractor.ocr.filter.ImageFilter;
import com.garodriguezlp.irimo.extractor.ocr.filter.ImageFilterPipeline;
import com.garodriguezlp.irimo.extractor.ocr.parser.FinancialRecordParser;
import com.garodriguezlp.irimo.extractor.ocr.processor.OcrProcessor;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OcrExtractorsConfig {

  @Bean
  public OcrFinancialRecordExtractor nuColombiaOcrFinancialRecordExtractor(
      ImageFilter nuColombiaImageCropFilter,
      OcrProcessor spanishOcrProcessor,
      FinancialRecordParser nuColombiaFinancialRecordParser) {
    return new OcrFinancialRecordExtractor(
        new ImageFilterPipeline(List.of(nuColombiaImageCropFilter)),
        spanishOcrProcessor,
        nuColombiaFinancialRecordParser
    );
  }

}
