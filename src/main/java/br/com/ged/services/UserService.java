package br.com.ged.services;

import br.com.ged.domains.User;
import br.com.ged.domains.dto.CreateUserRequest;
import br.com.ged.domains.dto.UserRequest;
import br.com.ged.mappers.CreateUserMapper;
import br.com.ged.repositories.UserRepository;
import br.com.ged.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final CreateUserMapper mapper;

    public UserService(UserRepository userRepository, CreateUserMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.jwtService = new JwtService();
    }

    public String login(UserRequest request) {
        try {
                Optional<User> user = userRepository.findByUsername(request.login());
                if (user.get().getUsername().equals(request.login()) && user.get().getPassword().equals(request.password())) {
                    return jwtService.gerarToken(request.login());
                }
                throw new RuntimeException("Usuário ou senha inválidos");
            }catch (Exception exception){
                throw new RuntimeException(exception.getMessage());
            }
        }

    public ResponseEntity<HttpStatus> createUser(CreateUserRequest createUserRequest) {

        User user = mapper.toUser(createUserRequest);

        if(userRepository.findByUsername(createUserRequest.username()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }else {
            userRepository.save(user);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
