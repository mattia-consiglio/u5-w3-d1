package mattiaconsiglio.u5w3d1.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import mattiaconsiglio.u5w3d1.dtos.EmployeeDTO;
import mattiaconsiglio.u5w3d1.entities.Employee;
import mattiaconsiglio.u5w3d1.exceptions.BadRequestException;
import mattiaconsiglio.u5w3d1.exceptions.RecordNotFoundException;
import mattiaconsiglio.u5w3d1.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private Cloudinary cloudinary;

    public Employee createEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByUsernameAndEmail(employeeDTO.username(), employeeDTO.email())) {
            throw new BadRequestException("Username and email already in use");
        } else if (employeeRepository.existsByUsername(employeeDTO.username())) {
            throw new BadRequestException("Username already in use");
        } else if (employeeRepository.existsByEmail(employeeDTO.email())) {
            throw new BadRequestException("Email already in use");
        }
        String photoUrl = "https://ui-avatars.com/api/?name=" + employeeDTO.firstName().charAt(0) + "+" + employeeDTO.lastName().charAt(0);
        return employeeRepository.save(new Employee(employeeDTO.username(), employeeDTO.firstName(), employeeDTO.lastName(), employeeDTO.email(), photoUrl));
    }


    public Page<Employee> getEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }


    public Employee getEmployee(UUID id) {
        return employeeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Employee", id));
    }


    public Employee updateEmployee(UUID id, EmployeeDTO employeeDTO) {
        Employee employee = this.getEmployee(id);
        if (employeeRepository.existsByUsernameAndEmail(employeeDTO.username(), employeeDTO.email()) && !employee.getUsername().equals(employeeDTO.username()) && !employee.getEmail().equals(employeeDTO.email())) {
            throw new BadRequestException("Username and email already in use");
        } else if (employeeRepository.existsByUsername(employeeDTO.username()) && !employee.getUsername().equals(employeeDTO.username())) {
            throw new BadRequestException("Username already in use");
        } else if (employeeRepository.existsByEmail(employeeDTO.email()) && !employee.getEmail().equals(employeeDTO.email())) {
            throw new BadRequestException("Email already in use");
        }
        String photoUrl = "https://ui-avatars.com/api/?name=" + employeeDTO.firstName().charAt(0) + "+" + employeeDTO.lastName().charAt(0);
        if (!employee.getPhotoUrl().startsWith("https://ui-avatars.com/api/")) {
            photoUrl = employee.getPhotoUrl();
        }
        employee.setUsername(employeeDTO.username());
        employee.setFirstName(employeeDTO.firstName());
        employee.setLastName(employeeDTO.lastName());
        employee.setEmail(employeeDTO.email());
        employee.setPhotoUrl(photoUrl);
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(UUID id) {
        Employee employee = this.getEmployee(id);
        employeeRepository.delete(employee);
    }


    public Employee updateEmployeePhoto(UUID id, MultipartFile photo) throws IOException {
        Employee employee = this.getEmployee(id);
        String url = (String) cloudinary.uploader().upload(photo.getBytes(), ObjectUtils.emptyMap()).get("url");
        employee.setPhotoUrl(url);
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeByUsernameOrEmail(String usernameOrEmail) {
        return employeeRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(() -> new RecordNotFoundException("Employee with usernameOrEmail/email " + usernameOrEmail + " not found"));
    }
}
