package com.grammr.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.grammr.domain.enums.features.Feature;
import com.grammr.domain.enums.features.FeatureType;
import java.io.IOException;
import java.util.Map;

public class FeatureDeserializer extends JsonDeserializer<Map<FeatureType, Feature>> {

  @Override
  public Map<FeatureType, Feature> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonMappingException {
    return Map.of();
  }
}
