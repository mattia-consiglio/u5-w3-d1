package mattiaconsiglio.u5w3d1.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import mattiaconsiglio.u5w3d1.dtos.EmployeePasswordDTO;
import mattiaconsiglio.u5w3d1.dtos.EmployeeRequestDTO;
import mattiaconsiglio.u5w3d1.entities.Employee;
import mattiaconsiglio.u5w3d1.exceptions.BadRequestException;
import mattiaconsiglio.u5w3d1.exceptions.RecordNotFoundException;
import mattiaconsiglio.u5w3d1.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder bCrypt;

    public Employee createEmployee(EmployeeRequestDTO employeeRequestDTO) {
        if (employeeRepository.existsByUsernameAndEmail(employeeRequestDTO.username(), employeeRequestDTO.email())) {
            throw new BadRequestException("Username and email already in use");
        } else if (employeeRepository.existsByUsername(employeeRequestDTO.username())) {
            throw new BadRequestException("Username already in use");
        } else if (employeeRepository.existsByEmail(employeeRequestDTO.email())) {
            throw new BadRequestException("Email already in use");
        }
        String photoUrl = "https://ui-avatars.com/api/?name=" + employeeRequestDTO.firstName().charAt(0) + "+" + employeeRequestDTO.lastName().charAt(0);
        return employeeRepository.save(new Employee(employeeRequestDTO.username(), employeeRequestDTO.firstName(), employeeRequestDTO.lastName(), employeeRequestDTO.email(), photoUrl, bCrypt.encode(employeeRequestDTO.password())));
    }


    public Page<Employee> getEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }


    public Employee getEmployee(UUID id) {
        return employeeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Employee", id));
    }


    public Employee updateEmployee(UUID id, EmployeeRequestDTO employeeRequestDTO) {
        Employee employee = this.getEmployee(id);
        if (employeeRepository.existsByUsernameAndEmail(employeeRequestDTO.username(), employeeRequestDTO.email()) && !employee.getUsername().equals(employeeRequestDTO.username()) && !employee.getEmail().equals(employeeRequestDTO.email())) {
            throw new BadRequestException("Username and email already in use");
        } else if (employeeRepository.existsByUsername(employeeRequestDTO.username()) && !employee.getUsername().equals(employeeRequestDTO.username())) {
            throw new BadRequestException("Username already in use");
        } else if (employeeRepository.existsByEmail(employeeRequestDTO.email()) && !employee.getEmail().equals(employeeRequestDTO.email())) {
            throw new BadRequestException("Email already in use");
        }
        String photoUrl = "https://ui-avatars.com/api/?name=" + employeeRequestDTO.firstName().charAt(0) + "+" + employeeRequestDTO.lastName().charAt(0);
        if (!employee.getPhotoUrl().startsWith("https://ui-avatars.com/api/")) {
            photoUrl = employee.getPhotoUrl();
        }
        employee.setUsername(employeeRequestDTO.username());
        employee.setFirstName(employeeRequestDTO.firstName());
        employee.setLastName(employeeRequestDTO.lastName());
        employee.setEmail(employeeRequestDTO.email());
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

    public Employee updateEmployeePassword(UUID id, EmployeePasswordDTO employeeDTO) {
        Employee employee = this.getEmployee(id);
        employee.setPassword(employeeDTO.password());
        return employeeRepository.save(employee);
    }
}
