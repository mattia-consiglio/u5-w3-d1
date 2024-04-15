package mattiaconsiglio.u5w3d1.controllers;

import mattiaconsiglio.u5w3d1.dtos.JWTDTO;
import mattiaconsiglio.u5w3d1.dtos.LoginAuthDTO;
import mattiaconsiglio.u5w3d1.exceptions.BadRequestException;
import mattiaconsiglio.u5w3d1.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public JWTDTO login(@RequestBody @Validated LoginAuthDTO loginAuthDTO, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException("Invalid data", validation.getAllErrors());
        }
        return authService.login(loginAuthDTO);
    }
}
