package com.grammr.port.rest.uri;

public interface MorphologyURIProvider {

  /**
   * This interface is a workaround for the different types of request routing when
   * running the application against a local docker-compose environment vs within
   * a kubernetes cluster.
   * @param languageCode The language code for the language to be analyzed.
   * @return URI to route the request to.
   */

  String provideUri(String languageCode);
}
