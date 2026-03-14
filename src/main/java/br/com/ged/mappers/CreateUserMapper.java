package br.com.ged.mappers;

import br.com.ged.domains.User;
import br.com.ged.domains.dto.CreateUserRequest;
import org.springframework.stereotype.Component;

@Component
public class CreateUserMapper {

    public User toUser(CreateUserRequest createUserRequest) {
        return new User(createUserRequest.username(), createUserRequest.password(), createUserRequest.profile());
    }
}
