package com.grammr.domain.entity;

import com.grammr.domain.User;
import com.grammr.domain.enums.LanguageCode;

public class UserSpec {

  public static User.UserBuilder valid() {
    return User.builder()
        .id(1)
        .telegramId(1)
        .languageSpoken(LanguageCode.EN)
        .languageLearned(LanguageCode.DE);
  }

}
