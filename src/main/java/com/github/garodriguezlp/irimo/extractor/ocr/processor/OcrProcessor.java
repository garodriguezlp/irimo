package com.github.garodriguezlp.irimo.extractor.ocr.processor;

import java.awt.image.BufferedImage;

public interface OcrProcessor {

  String performOcr(BufferedImage image);
}
