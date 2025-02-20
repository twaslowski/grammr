package com.grammr.port.inbound;

import com.grammr.domain.entity.User;
import com.grammr.domain.exception.InvalidInputException;
import com.grammr.domain.exception.UserAlreadyExistsException;
import com.grammr.port.dto.UserRegistrationRequest;
import com.grammr.port.dto.UserRegistrationResponse;
import com.grammr.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

  private final UserService userService;

  @PostMapping("/user")
  public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
    try {
      log.info("Registering user with username: {}", request.username());
      UserRegistrationResponse registrationResponse = userService.registerUser(request);

      log.info("User registered successfully with id: {}", registrationResponse.userId());
      var headers = headersWithSessionCookie(registrationResponse.sessionCookie());

      return ResponseEntity.status(201)
          .headers(headers)
          .body(registrationResponse);
    } catch (UserAlreadyExistsException e) {
      return ResponseEntity
          .status(HttpStatus.CONFLICT)
          .body(new ErrorResponse("User with this username already exists"));
    } catch (Exception e) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(new ErrorResponse(e.getMessage()));
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody UserRegistrationRequest request,
                                 @AuthenticationPrincipal User user) {
    if (user != null) {
      return ResponseEntity.ok().build();
    }
    try {
      String sessionToken = userService.login(request);
      var headers = headersWithSessionCookie(sessionToken);
      return ResponseEntity.ok().headers(headers).build();
    } catch (InvalidInputException e) {
      log.info("Login failed for user {} due to invalid input: {}", request.username(), request.password());
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body(new ErrorResponse("Invalid username or password"));
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(@AuthenticationPrincipal User user) {
    if (user != null) {
      userService.logout(user);
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @GetMapping("/user")
  public ResponseEntity<User> getUser(@AuthenticationPrincipal User user) {
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    return ResponseEntity.ok().body(user);
  }

  private HttpHeaders headersWithSessionCookie(String sessionCookie) {
    ResponseCookie responseCookie = responseCookieFrom(sessionCookie);
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());
    return headers;
  }

  private ResponseCookie responseCookieFrom(String sessionCookie) {
    return ResponseCookie.from("session_token", sessionCookie)
        .sameSite("None")
        .secure(true)
        .httpOnly(true)
        .path("/")
        .maxAge(86400)
        .build();
  }
}
