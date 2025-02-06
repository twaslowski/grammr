package com.grammr.domain.value.language;

import com.grammr.domain.enums.PartOfSpeechTag;
import java.util.List;

public record Inflections(PartOfSpeechTag partOfSpeech,
                          String lemma,
                          List<Inflection> inflections) {

}
