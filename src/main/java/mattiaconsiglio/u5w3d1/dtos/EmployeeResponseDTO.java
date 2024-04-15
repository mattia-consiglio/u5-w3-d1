package mattiaconsiglio.u5w3d1.dtos;

import java.util.UUID;

public record EmployeeResponseDTO(
        UUID id,
        String username,
        String firstName,
        String lastName,
        String email,
        String photoUrl
) {
}
