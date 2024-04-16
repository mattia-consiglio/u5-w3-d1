package mattiaconsiglio.u5w3d1.services;

import mattiaconsiglio.u5w3d1.dtos.DeviceDTO;
import mattiaconsiglio.u5w3d1.dtos.NewDeviceDTO;
import mattiaconsiglio.u5w3d1.entities.Device;
import mattiaconsiglio.u5w3d1.entities.DeviceStatus;
import mattiaconsiglio.u5w3d1.entities.Employee;
import mattiaconsiglio.u5w3d1.exceptions.BadRequestException;
import mattiaconsiglio.u5w3d1.exceptions.RecordNotFoundException;
import mattiaconsiglio.u5w3d1.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private EmployeeService employeeService;


    public Device addDevice(NewDeviceDTO deviceDTO) {
        return deviceRepository.save(new Device(deviceDTO.name(), deviceDTO.type(), deviceDTO.description(), DeviceStatus.AVILABLE));
    }


    public Page<Device> getDevices(Pageable pageable) {
        return deviceRepository.findAll(pageable);
    }


    public Device getDevice(UUID id) {
        return deviceRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Device", id));
    }

    public Device updateDevice(UUID id, DeviceDTO deviceDTO) {
        DeviceStatus status = DeviceStatus.valueOf(deviceDTO.status());
        Device device = this.getDevice(id);
        device.setName(deviceDTO.name());
        device.setType(deviceDTO.type());
        device.setDescription(deviceDTO.description());
        if (status == DeviceStatus.ASSIGNED && deviceDTO.employeeId() == null) {
            throw new BadRequestException("Employee is required for assigning a device");
        } else {
            device.setStatus(status);
        }

        if (deviceDTO.employeeId() != null) {
            Employee employee = employeeService.getEmployee(deviceDTO.employeeId());
            device.setEmployee(employee);
            if (status == DeviceStatus.DISPOSED) {
                throw new BadRequestException("Device can't be disposed if it's assigned to an employee");
            }

            if (device.getStatus() == DeviceStatus.AVILABLE) {
                device.setStatus(DeviceStatus.ASSIGNED);
            }
        } else {
            device.setEmployee(null);
        }

        return deviceRepository.save(device);
    }

    public void deleteDevice(UUID id) {
        Device device = this.getDevice(id);
        deviceRepository.delete(device);
    }


    public Device assignDeviceToEmployee(UUID id, UUID employeeId) {
        Device device = this.getDevice(id);
        Employee employee = employeeService.getEmployee(employeeId);
        device.setEmployee(employee);
        return deviceRepository.save(device);
    }

    public Page<Device> getDevicesByEmployee(Employee employee, Pageable pageable) {
        return deviceRepository.findAllByEmployee(employee, pageable);
    }
}
