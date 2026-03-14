package br.com.ged.controllers.interfaces;

import br.com.ged.domains.dto.CreateUserRequest;
import br.com.ged.domains.dto.UserRequest;
import br.com.ged.domains.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface AuthControllerInterface {

     ResponseEntity<UserResponse> login(UserRequest userRequest);

     ResponseEntity<HttpStatus> createUser(CreateUserRequest createUserRequest);
}
