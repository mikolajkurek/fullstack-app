package org.example.pasir_kurek_mikolaj.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
