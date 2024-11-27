package com.grammr.service.language.translation.literal;

import com.grammr.domain.value.language.TokenTranslation;
import java.util.List;

public interface LiteralTranslationService {

  List<TokenTranslation> translateTokens(String phrase, List<String> words);
}

