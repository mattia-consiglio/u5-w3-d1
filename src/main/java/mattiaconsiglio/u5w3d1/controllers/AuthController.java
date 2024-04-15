package mattiaconsiglio.u5w3d1.controllers;

import mattiaconsiglio.u5w3d1.dtos.EmployeeRequestDTO;
import mattiaconsiglio.u5w3d1.dtos.JWTDTO;
import mattiaconsiglio.u5w3d1.dtos.LoginAuthDTO;
import mattiaconsiglio.u5w3d1.entities.Employee;
import mattiaconsiglio.u5w3d1.exceptions.BadRequestException;
import mattiaconsiglio.u5w3d1.services.AuthService;
import mattiaconsiglio.u5w3d1.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("login")
    public JWTDTO login(@RequestBody @Validated LoginAuthDTO loginAuthDTO, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException("Invalid data", validation.getAllErrors());
        }
        return authService.login(loginAuthDTO);
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody @Validated EmployeeRequestDTO employeeRequestDTO, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException("Invalid data", validation.getAllErrors());
        }
        return employeeService.createEmployee(employeeRequestDTO);
    }
}
