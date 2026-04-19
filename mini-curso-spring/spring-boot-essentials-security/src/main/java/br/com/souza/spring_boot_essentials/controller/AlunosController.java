package br.com.souza.spring_boot_essentials.controller;

import br.com.souza.spring_boot_essentials.database.model.AvaliacaoesFisicasEntity;
import br.com.souza.spring_boot_essentials.dto.AlunoDto;
import br.com.souza.spring_boot_essentials.exception.BadRequestException;
import br.com.souza.spring_boot_essentials.exception.NotFoundException;
import br.com.souza.spring_boot_essentials.service.AlunosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/alunos")
@RequiredArgsConstructor
@Validated
public class AlunosController {

    private final AlunosService alunosService;

    @PreAuthorize("#alunoId == authentication.principal.id")
    @GetMapping("/{alunoId}/avaliacao")
    public AvaliacaoesFisicasEntity getAvaliacaoFisica(@PathVariable Integer alunoId) throws NotFoundException {
        return alunosService.getAlunoAvalicacao(alunoId);
    }

    @DeleteMapping("/{alunoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerAluno(@PathVariable Integer alunoId) throws Exception {
        alunosService.deletarAluno(alunoId);
    }
}
