package com.github.garodriguezlp.irimo.extractor.ocr.filter;

import java.awt.image.BufferedImage;

public interface ImageFilter {

  BufferedImage apply(BufferedImage image);
}
