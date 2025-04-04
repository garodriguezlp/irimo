package com.github.garodriguezlp.irimo.extractor.ocr.parser.util;

import java.util.ArrayList;
import java.util.List;

public class TextChunkSplitter {

  public static List<List<String>> splitIntoChunksByEmptyLines(String input) {
    String[] lines = input.trim().split("\n");
    List<List<String>> rawRecords = new ArrayList<>();
    List<String> currentBucket = new ArrayList<>();
    rawRecords.add(currentBucket);

    for (String line : lines) {
      line = line.trim();

      if (line.isEmpty()) {
        currentBucket = new ArrayList<>();
        rawRecords.add(currentBucket);
      } else {
        currentBucket.add(line);
      }
    }

    rawRecords.removeIf(List::isEmpty);

    return rawRecords;
  }
}
