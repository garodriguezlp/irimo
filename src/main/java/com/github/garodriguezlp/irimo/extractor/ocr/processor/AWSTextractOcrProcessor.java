package com.github.garodriguezlp.irimo.extractor.ocr.processor;

import com.github.garodriguezlp.irimo.extractor.ocr.exception.OcrProcessingException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.BlockType;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextRequest;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextResponse;
import software.amazon.awssdk.services.textract.model.Document;
import software.amazon.awssdk.services.textract.model.TextractException;

public class AWSTextractOcrProcessor implements OcrProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(AWSTextractOcrProcessor.class);
  private final float verticalSectionBreakThreshold;

  public AWSTextractOcrProcessor(float verticalSectionBreakThreshold) {
    this.verticalSectionBreakThreshold = verticalSectionBreakThreshold;
  }

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
    float lastTop = 0f;
    float currentTop;

    for (Block block : blocks) {
      if (block.blockType() == BlockType.LINE && block.confidence() >= 93) {
        currentTop = block.geometry().boundingBox().top();
        float verticalDistance = currentTop - lastTop;

        LOGGER.trace("Vertical distance: {}, current text: {}, confidence: {}", verticalDistance,
        block.text(), block.confidence());
        if (verticalDistance > verticalSectionBreakThreshold) {
          extractedText.append("\n");
        }

        extractedText.append(block.text()).append("\n");
        lastTop = currentTop;
      }
    }

    return extractedText.toString();
  }
}
