package br.com.souza.spring_boot_essentials.service;

import br.com.souza.spring_boot_essentials.config.TokenProvider;
import br.com.souza.spring_boot_essentials.database.model.AlunosEntity;
import br.com.souza.spring_boot_essentials.database.model.RoleEntity;
import br.com.souza.spring_boot_essentials.database.repository.IAlunosRepository;
import br.com.souza.spring_boot_essentials.database.repository.IRoleRepository;
import br.com.souza.spring_boot_essentials.dto.LoginRequestDto;
import br.com.souza.spring_boot_essentials.dto.RegisterRequestDto;
import br.com.souza.spring_boot_essentials.dto.TokenResponseDTO;
import br.com.souza.spring_boot_essentials.enums.RoleTypeEnum;
import br.com.souza.spring_boot_essentials.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final IAlunosRepository alunosRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    @Value("${jwt.expiration}")
    private long expirationTime;

    public void register(RegisterRequestDto registerRequestDto) throws BadRequestException {
        if (alunosRepository.findByEmail(registerRequestDto.getEmail()).isPresent()) {
            throw new BadRequestException("Email já cadastrado");
        }

        RoleEntity role = roleRepository.findByNome(RoleTypeEnum.ALUNO.name())
                .orElseGet(() -> roleRepository.save(RoleEntity.builder()
                        .nome(RoleTypeEnum.ALUNO.name())
                        .build()));

        alunosRepository.save(AlunosEntity.builder()
                .nome(registerRequestDto.getNome())
                .email(registerRequestDto.getEmail())
                .senha(passwordEncoder.encode(registerRequestDto.getSenha()))
                .roles(Set.of(role))
                .build());
    }

    public TokenResponseDTO login(LoginRequestDto loginDto) throws Exception{
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getSenha())
            );

            String token = tokenProvider.generateToken(authentication);
            return TokenResponseDTO.builder()
                    .token(token)
                    .type("Bearer")
                    .expiration(expirationTime)
                    .build();
        }catch (BadCredentialsException e){
            throw new BadRequestException("Credenciais inválidas");
        }catch (Exception e){
            throw new Exception("Erro interno inesperado: " + e.getMessage());
        }
    }
}
