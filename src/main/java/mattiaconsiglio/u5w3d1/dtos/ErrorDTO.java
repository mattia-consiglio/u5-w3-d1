package mattiaconsiglio.u5w3d1.dtos;

import java.time.LocalDateTime;

public record ErrorDTO(String message, LocalDateTime timestamp) {
}
