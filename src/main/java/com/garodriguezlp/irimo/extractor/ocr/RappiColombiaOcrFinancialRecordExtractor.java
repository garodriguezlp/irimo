package com.garodriguezlp.irimo.extractor.ocr;

import com.garodriguezlp.irimo.extractor.ocr.filter.ImageFilter;
import com.garodriguezlp.irimo.extractor.ocr.filter.ImageFilterPipeline;
import com.garodriguezlp.irimo.extractor.ocr.parser.RappiColombiaFinancialRecordParser;
import com.garodriguezlp.irimo.extractor.ocr.processor.AWSTextractOcrProcessor;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RappiColombiaOcrFinancialRecordExtractor extends OcrFinancialRecordExtractor {

  public RappiColombiaOcrFinancialRecordExtractor(
      ImageFilter rappiColombiaImageCropFilter,
      AWSTextractOcrProcessor rappiAWSTextractOcrProcessor ,
      RappiColombiaFinancialRecordParser parser) {
    super(
        new ImageFilterPipeline(List.of(rappiColombiaImageCropFilter)),
        rappiAWSTextractOcrProcessor,
        parser);
  }

  @Override
  public String getIdentifier() {
    return "rappi";
  }
}