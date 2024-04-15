package mattiaconsiglio.u5w3d1.repositories;

import mattiaconsiglio.u5w3d1.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameAndEmail(String username, String email);

    Optional<Employee> findByUsernameOrEmail(String username, String email);
}
