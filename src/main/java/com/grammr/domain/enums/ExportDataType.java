package com.grammr.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExportDataType {

  CSV("application/zip", "zip"),
  APKG("application/octet-stream", "apkg"),
  DB("application/octet-stream", "db");

  private final String mediaType;
  private final String extension;
}
