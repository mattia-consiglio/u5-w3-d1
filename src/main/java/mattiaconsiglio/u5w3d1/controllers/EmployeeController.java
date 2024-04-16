package mattiaconsiglio.u5w3d1.controllers;

import mattiaconsiglio.u5w3d1.dtos.EmployeePasswordDTO;
import mattiaconsiglio.u5w3d1.dtos.EmployeeRequestDTO;
import mattiaconsiglio.u5w3d1.entities.Employee;
import mattiaconsiglio.u5w3d1.exceptions.BadRequestException;
import mattiaconsiglio.u5w3d1.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;


    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Employee> getEmployees(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "username") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return employeeService.getEmployees(pageable);
    }

    @GetMapping("me")
    public Employee getCurrentEmployee(@AuthenticationPrincipal Employee currentEmployee) {
        return currentEmployee;
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee getEmployee(@PathVariable UUID id) {
        return employeeService.getEmployee(id);
    }


    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee updateEmployee(@PathVariable UUID id, @RequestBody @Validated EmployeeRequestDTO employeeRequestDTO, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException("Invalid data", validation.getAllErrors());
        }
        return employeeService.updateEmployee(id, employeeRequestDTO);
    }

    @PutMapping("me")
    public Employee updateCurrenEmployee(@AuthenticationPrincipal Employee currentEmployee, @RequestBody @Validated EmployeeRequestDTO employeeRequestDTO, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException("Invalid data", validation.getAllErrors());
        }
        return employeeService.updateEmployee(currentEmployee.getId(), employeeRequestDTO);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
    }

    @DeleteMapping("me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCurrentEmployee(@AuthenticationPrincipal Employee currentEmployee) {
        employeeService.deleteEmployee(currentEmployee.getId());
    }


    @PutMapping("{id}/photo")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee updateEmployeePhoto(@PathVariable UUID id, @RequestParam("photo") MultipartFile photo) throws IOException {
        return employeeService.updateEmployeePhoto(id, photo);
    }

    @PutMapping("me/photo")
    public Employee updateCurrentEmployeePhoto(@AuthenticationPrincipal Employee currentEmployee, @RequestParam("photo") MultipartFile photo) throws IOException {
        return employeeService.updateEmployeePhoto(currentEmployee.getId(), photo);
    }

    @PutMapping("{id}/password")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee updateEmployeePassword(@PathVariable UUID id, @RequestBody @Validated EmployeePasswordDTO employeeDTO, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException("Invalid data", validation.getAllErrors());
        }
        return employeeService.updateEmployeePassword(id, employeeDTO);
    }

    @PutMapping("me/password")
    public Employee updateCurrentEmployeePassword(@AuthenticationPrincipal Employee currentEmployee, @RequestBody @Validated EmployeePasswordDTO employeeDTO, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException("Invalid data", validation.getAllErrors());
        }
        return employeeService.updateEmployeePassword(currentEmployee.getId(), employeeDTO);
    }

}
