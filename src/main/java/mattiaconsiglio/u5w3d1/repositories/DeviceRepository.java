package mattiaconsiglio.u5w3d1.repositories;

import mattiaconsiglio.u5w3d1.entities.Device;
import mattiaconsiglio.u5w3d1.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {
    Page<Device> findAllByEmployee(Employee employee, Pageable pageable);
}
