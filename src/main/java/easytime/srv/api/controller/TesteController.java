package easytime.srv.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class TesteController {

    @GetMapping()
    @Operation(summary = "Rota teste", description = "Rota para verificar se api está no ar")
    @SecurityRequirement(name = "bearer-key")
    public String hello(){
        return "Hello World";
    }
}
