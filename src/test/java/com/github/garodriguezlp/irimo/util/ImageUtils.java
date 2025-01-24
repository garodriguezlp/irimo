package com.github.garodriguezlp.irimo.util;

import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public final class ImageUtils {

  private ImageUtils() {
    // Utility class, hide constructor
  }

  public static BufferedImage loadImageFromClasspath(String resourcePath) throws IOException {
    byte[] bytes = IOUtils.resourceToByteArray(resourcePath);
    return ImageIO.read(new ByteArrayInputStream(bytes));
  }
}
