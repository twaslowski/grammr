package com.grammr.config.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class SessionCookieUtils {

  public static final String ANON_COOKIE_NAME = "anon_session_id";
  public static final Duration COOKIE_DURATION = Duration.ofDays(90);

  public static String generateSessionId() {
    return UUID.randomUUID().toString();
  }

  public static Optional<String> findSessionId(HttpServletRequest request) {
    if (request.getCookies() == null) {
      return Optional.empty();
    }
    return Arrays.stream(request.getCookies())
        .filter(c -> ANON_COOKIE_NAME.equals(c.getName()))
        .map(Cookie::getValue)
        .findFirst();
  }

  public static void addSessionCookie(HttpServletResponse response, String sessionId) {
    Cookie cookie = new Cookie(ANON_COOKIE_NAME, sessionId);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge((int) COOKIE_DURATION.getSeconds());
    response.addCookie(cookie);
  }
}
