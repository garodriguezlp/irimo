# Tweaking Tesseract for Better Results

To improve the OCR results using Tess4J, you can try several configurations and techniques:

1. **Page Segmentation Mode (TessPageSegMode)**:
   - This setting determines how Tesseract splits the image into text lines and words. Experiment with different modes to see
   which works best for your image. For example, try `PSM_SINGLE_BLOCK` or `PSM_AUTO`.

2. **Image Preprocessing**:
   - Enhance the image quality before OCR. You can apply techniques like binarization, noise reduction, and contrast adjustment
   using image processing libraries like OpenCV.

3. **DPI Setting**:
   - Ensure the image is at an optimal DPI (usually 300 DPI is recommended for OCR).

4. **Language Configuration**:
   - Make sure the Spanish language data is correctly installed and configured.

5. **Character Whitelist/Blacklist**:
   - Use `setTessVariable("tessedit_char_whitelist", "0123456789.$")` to focus on characters that are likely to appear in your text.

6. **Iterate with Different Levels (TessPageIteratorLevel)**:
   - Try different iterator levels such as `RIL_WORD` or `RIL_SYMBOL` to see if they yield better results for specific parts of the text.

7. **Try Different OCR Engines**:
   - Tesseract has different OCR engines (e.g., LSTM-based). You can switch between them using `setOcrEngineMode`.

8. **Text Line Justification (TessParagraphJustification)**:
   - Adjusting justification might help if the text alignment is causing issues.

9. **Use Config Files**:
   - Tesseract allows custom configuration files where you can specify multiple parameters at once.

By experimenting with these settings, you should be able to achieve better OCR accuracy with Tess4J.
