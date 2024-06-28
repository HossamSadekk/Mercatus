package com.sideproject.mercatus.api.controller.auth;

import com.sideproject.mercatus.api.model.RegistrationBody;
import com.sideproject.mercatus.exceptions.UserAlreadyExistException;
import com.sideproject.mercatus.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    public UserService userService;

    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationBody registrationBody) {
        try {
            userService.registerUser(registrationBody);
           return ResponseEntity.ok().build();
        } catch (UserAlreadyExistException e) {
           return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
