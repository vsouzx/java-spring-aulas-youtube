package br.com.souza.spring_boot_essentials.database.repository;

import br.com.souza.spring_boot_essentials.database.model.AlunosEntity;
import br.com.souza.spring_boot_essentials.database.model.AvaliacaoesFisicasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IAlunosRepository extends JpaRepository<AlunosEntity, Integer> {

    Optional<AlunosEntity> findByEmail(String email);

    @Query(value = "SELECT a FROM AlunosEntity a JOIN FETCH a.avaliacaoFisica")
    Optional<AlunosEntity> findByIdFetch(Integer alunoId);
}
