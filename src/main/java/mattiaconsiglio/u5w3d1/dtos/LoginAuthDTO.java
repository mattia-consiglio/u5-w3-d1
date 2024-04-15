package mattiaconsiglio.u5w3d1.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginAuthDTO(
        @NotBlank
        String usernameOrEmail,
        @NotBlank
        String password) {
}
