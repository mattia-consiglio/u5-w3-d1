package mattiaconsiglio.u5w3d1.services;

import mattiaconsiglio.u5w3d1.dtos.JWTDTO;
import mattiaconsiglio.u5w3d1.dtos.LoginAuthDTO;
import mattiaconsiglio.u5w3d1.entities.Employee;
import mattiaconsiglio.u5w3d1.exceptions.UnauthorizedException;
import mattiaconsiglio.u5w3d1.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JWTTools jwtTools;

    public JWTDTO login(LoginAuthDTO loginAuthDTO) {
        Employee employee = employeeService.getEmployeeByUsernameOrEmail(loginAuthDTO.usernameOrEmail());
        if (employee == null || !employee.getPassword().equals(loginAuthDTO.password())) {
            throw new UnauthorizedException("Credentials not valid. Try login again");
        }
        return new JWTDTO(jwtTools.generateToken(employee));
    }
}
