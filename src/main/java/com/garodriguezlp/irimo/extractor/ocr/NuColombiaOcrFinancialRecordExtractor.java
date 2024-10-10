package com.garodriguezlp.irimo.extractor.ocr;

import com.garodriguezlp.irimo.extractor.ocr.filter.ImageFilter;
import com.garodriguezlp.irimo.extractor.ocr.filter.ImageFilterPipeline;
import com.garodriguezlp.irimo.extractor.ocr.parser.FinancialRecordParser;
import com.garodriguezlp.irimo.extractor.ocr.parser.NuColombiaFinancialRecordParser;
import com.garodriguezlp.irimo.extractor.ocr.processor.OcrProcessor;
import com.garodriguezlp.irimo.extractor.ocr.processor.SpanishTess4JOcrProcessor;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NuColombiaOcrFinancialRecordExtractor extends OcrFinancialRecordExtractor {

  public NuColombiaOcrFinancialRecordExtractor(
      ImageFilter nuColombiaImageCropFilter,
      SpanishTess4JOcrProcessor ocrProcessor,
      NuColombiaFinancialRecordParser parser) {
    super(
        new ImageFilterPipeline(List.of(nuColombiaImageCropFilter)),
        ocrProcessor,
        parser);
  }

  @Override
  public String getIdentifier() {
    return "nu";
  }
}
