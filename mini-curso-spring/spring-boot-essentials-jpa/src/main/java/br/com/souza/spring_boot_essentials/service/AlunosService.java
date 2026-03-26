package br.com.souza.spring_boot_essentials.service;

import br.com.souza.spring_boot_essentials.database.model.AlunosEntity;
import br.com.souza.spring_boot_essentials.database.model.AvaliacaoesFisicasEntity;
import br.com.souza.spring_boot_essentials.database.repository.IAlunosRepository;
import br.com.souza.spring_boot_essentials.dto.AlunoDto;
import br.com.souza.spring_boot_essentials.exception.BadRequestException;
import br.com.souza.spring_boot_essentials.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlunosService {

    private final IAlunosRepository alunosRepository;

    public void criarAluno(AlunoDto alunoDto) throws BadRequestException {
        AlunosEntity aluno = alunosRepository.findByEmail(alunoDto.getEmail())
                .orElse(null);

        if (aluno != null) {
            throw new BadRequestException("Aluno já cadastrado com este email");
        }

        alunosRepository.save(AlunosEntity.builder()
                .nome(alunoDto.getNome())
                .email(alunoDto.getEmail())
                .build());
    }

    public AvaliacaoesFisicasEntity getAlunoAvalicacao(Integer alunoId) throws NotFoundException{
        AlunosEntity aluno = alunosRepository.findById(alunoId)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));

        AvaliacaoesFisicasEntity avaliacao = aluno.getAvaliacaoFisica();
        if (avaliacao == null) {
            throw new NotFoundException("Avaliação física não encontrada para este aluno");
        }

        return avaliacao;
    }
}
