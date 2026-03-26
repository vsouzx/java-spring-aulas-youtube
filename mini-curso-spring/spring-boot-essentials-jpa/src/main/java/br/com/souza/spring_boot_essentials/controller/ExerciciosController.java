package br.com.souza.spring_boot_essentials.controller;

import br.com.souza.spring_boot_essentials.database.model.ExerciciosEntity;
import br.com.souza.spring_boot_essentials.dto.ExercicioDto;
import br.com.souza.spring_boot_essentials.service.ExerciciosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/exercicios")
@RequiredArgsConstructor
@Validated
public class ExerciciosController {

    private final ExerciciosService exerciciosService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ExerciciosEntity> findAll(){
        return exerciciosService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveExercicio(@Valid @RequestBody ExercicioDto exercicioDto){
        exerciciosService.save(exercicioDto);
    }

    @GetMapping("/grupos/{grupoMuscular}")
    @ResponseStatus(HttpStatus.OK)
    public List<ExerciciosEntity> getExerciciosByGrupoMuscular(@PathVariable String grupoMuscular){
        return exerciciosService.getExerciciosByGrupoMuscular(grupoMuscular);
    }
}
