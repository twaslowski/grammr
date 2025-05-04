package com.grammr.domain.value.language;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.enums.PartOfSpeechTag;
import java.util.List;

public record ParadigmDTO(String paradigmId,
                          PartOfSpeechTag partOfSpeech,
                          String lemma,
                          LanguageCode language,
                          List<Inflection> inflections) {

}
