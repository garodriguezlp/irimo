package com.garodriguezlp.irimo.extractor.ocr.processor;

import com.garodriguezlp.irimo.extractor.ocr.exception.OcrProcessingException;
import java.awt.image.BufferedImage;
import java.io.File;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SpanishTess4JOcrProcessor implements OcrProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpanishTess4JOcrProcessor.class);
  private static final String TESSDATA_RESOURCE_PATH = "tessdata";

  private final Tesseract tesseract;

  public SpanishTess4JOcrProcessor() {
    this.tesseract = new Tesseract();
    configureTesseract(this.tesseract);
  }

  @Override
  public String performOcr(BufferedImage image) {
    LOGGER.info("Starting OCR process on the provided image");
    try {
      String result = tesseract.doOCR(image);
      LOGGER.info("OCR process completed successfully");
      return result;
    } catch (TesseractException e) {
      throw new OcrProcessingException("OCR process failed", e);
    }
  }

  private static void configureTesseract(Tesseract tesseract) {
    File tessDataFolder = LoadLibs.extractTessResources(TESSDATA_RESOURCE_PATH);
    tesseract.setDatapath(tessDataFolder.getPath());
    tesseract.setLanguage("spa");
  }
}
