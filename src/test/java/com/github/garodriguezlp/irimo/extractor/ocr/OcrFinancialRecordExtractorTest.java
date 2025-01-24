package com.github.garodriguezlp.irimo.extractor.ocr;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.garodriguezlp.irimo.domain.FinancialRecord;
import com.github.garodriguezlp.irimo.extractor.ocr.filter.ImageFilterPipeline;
import com.github.garodriguezlp.irimo.extractor.ocr.parser.FinancialRecordParser;
import com.github.garodriguezlp.irimo.extractor.ocr.processor.OcrProcessor;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OcrFinancialRecordExtractorTest {

  @Mock
  private ImageFilterPipeline imageFilterPipeline;
  @Mock
  private OcrProcessor ocrProcessor;
  @Mock
  private FinancialRecordParser financialRecordParser;

  private OcrFinancialRecordExtractor financialRecordExtractor;

  @BeforeEach
  void setUp() {
    financialRecordExtractor = new OcrFinancialRecordExtractor(imageFilterPipeline, ocrProcessor,
        financialRecordParser) {
      @Override
      public String getIdentifier() {
        return "test";
      }
    };
  }

  @Test
  void testExtract(@TempDir Path tempDir) throws IOException {
    // given
    File image1 = createTestImageFile(tempDir, "image1.jpeg");
    File image2 = createTestImageFile(tempDir, "image2.jpeg");
    BufferedImage filteredImage = mock(BufferedImage.class);

    when(imageFilterPipeline.execute(any(BufferedImage.class))).thenReturn(filteredImage);
    when(ocrProcessor.performOcr(filteredImage)).thenReturn("mocked OCR text");
    FinancialRecord mockRecord = new FinancialRecord(null, "description", null, "source");
    when(financialRecordParser.extractRecords("mocked OCR text")).thenReturn(List.of(mockRecord));

    // when
    List<FinancialRecord> records = financialRecordExtractor.extract(List.of(image1, image2));

    // then
    assertThat(records).hasSize(2);
    verify(imageFilterPipeline, times(2)).execute(any(BufferedImage.class));
    verify(ocrProcessor, times(2)).performOcr(filteredImage);
    verify(financialRecordParser, times(2)).extractRecords("mocked OCR text");
  }

  private File createTestImageFile(Path directory, String fileName) throws IOException {
    Path imagePath = directory.resolve(fileName);
    BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = image.createGraphics();
    try {
      g2d.setColor(Color.WHITE);
      g2d.fillRect(0, 0, 100, 100);
      g2d.setColor(Color.BLACK);
      g2d.drawString("Test", 10, 50);
    } finally {
      g2d.dispose();
    }

    File imageFile = imagePath.toFile();
    ImageIO.write(image, "jpg", imageFile);
    return imageFile;
  }
}
