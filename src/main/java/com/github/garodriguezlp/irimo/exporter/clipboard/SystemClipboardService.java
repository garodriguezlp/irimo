package com.github.garodriguezlp.irimo.exporter.clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SystemClipboardService implements ClipboardService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SystemClipboardService.class);

  @Override
  public void copy(String data) {
    LOGGER.debug("Copying data to clipboard");
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    StringSelection selection = new StringSelection(data);
    clipboard.setContents(selection, null);
  }
}
