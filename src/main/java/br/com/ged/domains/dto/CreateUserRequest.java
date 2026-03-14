package br.com.ged.domains.dto;

import br.com.ged.domains.enums.Profiles;

public record CreateUserRequest(String username, String password, String confirmPassword, Profiles profile) {

}
