package br.com.souza.spring_boot_essentials.controller;

import br.com.souza.spring_boot_essentials.database.model.AvaliacaoesFisicasEntity;
import br.com.souza.spring_boot_essentials.database.model.ExerciciosEntity;
import br.com.souza.spring_boot_essentials.dto.AlunoDto;
import br.com.souza.spring_boot_essentials.dto.ExercicioDto;
import br.com.souza.spring_boot_essentials.exception.BadRequestException;
import br.com.souza.spring_boot_essentials.exception.NotFoundException;
import br.com.souza.spring_boot_essentials.service.AlunosService;
import br.com.souza.spring_boot_essentials.service.ExerciciosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/alunos")
@RequiredArgsConstructor
@Validated
public class AlunosController {

    private final AlunosService alunosService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarAluno(@Valid @RequestBody AlunoDto alunoDto) throws BadRequestException {
        alunosService.criarAluno(alunoDto);
    }

    @GetMapping("/{alunoId}/avaliacao")
    public AvaliacaoesFisicasEntity getAvaliacaoFisica(@PathVariable Integer alunoId) throws NotFoundException {
        return alunosService.getAlunoAvalicacao(alunoId);
    }
}
