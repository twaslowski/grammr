package com.grammr.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Deprecated
@RequiredArgsConstructor
public enum ExportDataType {

  /**
   * Will be replaced by actively syncing to an Anki Instance via AnkiConnect.
   */

  APKG("application/octet-stream", "apkg"),
  DB("application/octet-stream", "db");

  private final String mediaType;
  private final String extension;
}
