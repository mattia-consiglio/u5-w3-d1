package mattiaconsiglio.u5w3d1.repositories;

import mattiaconsiglio.u5w3d1.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {
}
