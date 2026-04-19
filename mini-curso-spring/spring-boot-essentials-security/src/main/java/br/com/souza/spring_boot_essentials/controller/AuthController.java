package br.com.souza.spring_boot_essentials.controller;

import br.com.souza.spring_boot_essentials.dto.LoginRequestDto;
import br.com.souza.spring_boot_essentials.dto.RegisterRequestDto;
import br.com.souza.spring_boot_essentials.dto.TokenResponseDTO;
import br.com.souza.spring_boot_essentials.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid RegisterRequestDto registerRequestDto) throws Exception{
        authService.register(registerRequestDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDTO login(@RequestBody @Valid LoginRequestDto loginRequestDto) throws Exception{
        return authService.login(loginRequestDto);
    }
}
