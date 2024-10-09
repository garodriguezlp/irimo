package com.garodriguezlp.irimo.extractor.ocr;


import com.garodriguezlp.irimo.extractor.ocr.filter.ImageFilter;
import com.garodriguezlp.irimo.extractor.ocr.filter.ImageFilterPipeline;
import com.garodriguezlp.irimo.extractor.ocr.parser.FinancialRecordParser;
import com.garodriguezlp.irimo.extractor.ocr.processor.OcrProcessor;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NuColombiaOcrFinancialRecordExtractor extends OcrFinancialRecordExtractor {

  public NuColombiaOcrFinancialRecordExtractor(
      ImageFilter nuColombiaImageCropFilter,
      OcrProcessor spanishOcrProcessor,
      FinancialRecordParser nuColombiaFinancialRecordParser) {
    super(
        new ImageFilterPipeline(List.of(nuColombiaImageCropFilter)),
        spanishOcrProcessor,
        nuColombiaFinancialRecordParser);
  }

  @Override
  public String getIdentifier() {
    return "nu";
  }
}
