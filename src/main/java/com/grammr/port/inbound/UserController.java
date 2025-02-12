package com.grammr.port.inbound;

import com.grammr.domain.exception.UserAlreadyExistsException;
import com.grammr.port.dto.UserRegistrationRequest;
import com.grammr.port.dto.UserRegistrationResponse;
import com.grammr.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

  private final UserService userService;

  @PostMapping("/user")
  public ResponseEntity<?> registerUser(
      @Valid @RequestBody UserRegistrationRequest request,
      HttpServletResponse response) {
    try {
      UserRegistrationResponse registrationResponse = userService.registerUser(request);

      Cookie sessionCookie = new Cookie("session_token", registrationResponse.sessionCookie());
      sessionCookie.setHttpOnly(true);
      sessionCookie.setSecure(true);
      sessionCookie.setPath("/");
      sessionCookie.setMaxAge(86400);
      response.addCookie(sessionCookie);

      return ResponseEntity.status(201).body(registrationResponse);
    } catch (UserAlreadyExistsException e) {
      return ResponseEntity
          .status(HttpStatus.CONFLICT)
          .body(new ErrorResponse("User with this email already exists"));
    } catch (Exception e) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(new ErrorResponse(e.getMessage()));
    }
  }
}
