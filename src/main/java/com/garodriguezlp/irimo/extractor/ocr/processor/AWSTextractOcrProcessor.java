package com.garodriguezlp.irimo.extractor.ocr.processor;

import com.garodriguezlp.irimo.extractor.ocr.exception.OcrProcessingException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.*;

@Service
public class AWSTextractOcrProcessor implements OcrProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(AWSTextractOcrProcessor.class);

  @Override
  public String performOcr(BufferedImage image) {
    LOGGER.info("Starting OCR process");
    try (TextractClient textractClient = TextractClient.create()) {
      SdkBytes imageBytes = convertImageToSdkBytes(image);
      DetectDocumentTextResponse result = detectDocumentText(textractClient, imageBytes);
      return extractTextFromBlocks(result.blocks());
    } catch (TextractException | IOException e) {
      throw new OcrProcessingException("OCR process failed", e);
    }
  }

  private SdkBytes convertImageToSdkBytes(BufferedImage image) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ImageIO.write(image, "png", outputStream);
    return SdkBytes.fromByteArray(outputStream.toByteArray());
  }

  private DetectDocumentTextResponse detectDocumentText(TextractClient textractClient,
      SdkBytes imageBytes) {
    Document document = Document.builder().bytes(imageBytes).build();
    DetectDocumentTextRequest request = DetectDocumentTextRequest.builder()
        .document(document)
        .build();
    return textractClient.detectDocumentText(request);
  }

  private String extractTextFromBlocks(List<Block> blocks) {
    StringBuilder extractedText = new StringBuilder();
    for (Block block : blocks) {
      if (block.blockType() == BlockType.LINE) {
        extractedText.append(block.text()).append("\n");
      }
    }
    return extractedText.toString();
  }
}
