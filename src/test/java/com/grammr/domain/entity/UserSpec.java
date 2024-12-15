package com.grammr.domain.entity;

import com.grammr.domain.entity.User.UserBuilder;
import com.grammr.domain.enums.LanguageCode;

public class UserSpec {

  public static UserBuilder valid() {
    return User.builder()
        .id(1)
        .chatId(1)
        .languageSpoken(LanguageCode.EN)
        .languageLearned(LanguageCode.DE);
  }
}
