package mattiaconsiglio.u5w3d1.dtos;

import jakarta.validation.constraints.NotBlank;

public record EmployeePasswordDTO(@NotBlank
                                  String password) {
}
