package com.garodriguezlp.irimo.extractor.ocr;

import com.garodriguezlp.irimo.extractor.ocr.filter.ImageFilter;
import com.garodriguezlp.irimo.extractor.ocr.filter.ImageFilterPipeline;
import com.garodriguezlp.irimo.extractor.ocr.parser.FinancialRecordParser;
import com.garodriguezlp.irimo.extractor.ocr.parser.RappiColombiaFinancialRecordParser;
import com.garodriguezlp.irimo.extractor.ocr.processor.AWSTextractOcrProcessor;
import com.garodriguezlp.irimo.extractor.ocr.processor.OcrProcessor;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RappiColombiaOcrFinancialRecordExtractor extends OcrFinancialRecordExtractor {

  public RappiColombiaOcrFinancialRecordExtractor(
      ImageFilter rappiColombiaImageCropFilter,
      AWSTextractOcrProcessor ocrProcessor ,
      RappiColombiaFinancialRecordParser parser) {
    super(
        new ImageFilterPipeline(List.of(rappiColombiaImageCropFilter)),
        ocrProcessor,
        parser);
  }

  @Override
  public String getIdentifier() {
    return "rappi";
  }
}
