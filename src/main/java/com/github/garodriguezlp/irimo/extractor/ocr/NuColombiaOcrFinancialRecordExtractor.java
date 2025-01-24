package com.github.garodriguezlp.irimo.extractor.ocr;

import com.github.garodriguezlp.irimo.extractor.ocr.filter.ImageFilter;
import com.github.garodriguezlp.irimo.extractor.ocr.filter.ImageFilterPipeline;
import com.github.garodriguezlp.irimo.extractor.ocr.parser.NuColombiaFinancialRecordParser;
import com.github.garodriguezlp.irimo.extractor.ocr.processor.AWSTextractOcrProcessor;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NuColombiaOcrFinancialRecordExtractor extends OcrFinancialRecordExtractor {

  public NuColombiaOcrFinancialRecordExtractor(
      ImageFilter nuColombiaImageCropFilter,
      AWSTextractOcrProcessor nuAWSTextractOcrProcessor,
      NuColombiaFinancialRecordParser parser) {
    super(
        new ImageFilterPipeline(List.of(nuColombiaImageCropFilter)),
        nuAWSTextractOcrProcessor,
        parser);
  }

  @Override
  public String getIdentifier() {
    return "nu";
  }
}
