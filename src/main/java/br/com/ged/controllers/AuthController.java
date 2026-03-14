package br.com.ged.controllers;

import br.com.ged.controllers.interfaces.AuthControllerInterface;
import br.com.ged.domains.dto.CreateUserRequest;
import br.com.ged.domains.dto.UserRequest;
import br.com.ged.domains.dto.UserResponse;
import br.com.ged.exceptions.InvalidCredentialsException;
import br.com.ged.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerInterface {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest userRequest) {
        try {
            System.out.println(userRequest);
            String token = userService.login(userRequest);
            return ResponseEntity.ok(new UserResponse(token));

        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UserResponse(e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UserResponse("Erro interno ao realizar login "));
        }

    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createUser(@RequestBody CreateUserRequest createUserRequest) {
        try {
            return userService.createUser(createUserRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}