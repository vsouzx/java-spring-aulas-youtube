package br.com.souza.spring_boot_essentials.database.repository;

import br.com.souza.spring_boot_essentials.database.model.ExerciciosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IExerciciosRepository extends JpaRepository<ExerciciosEntity, Integer> {

    List<ExerciciosEntity> findAllByGrupoMuscular(String grupoMuscular);

    @Query(value = """
        SELECT e
        FROM ExerciciosEntity e
        WHERE UPPER(e.grupoMuscular) = UPPER(:grupoMuscular)
    """)
    List<ExerciciosEntity> findAllByGrupoMuscularJpql(@Param("grupoMuscular") String grupoMuscular);

    @NativeQuery(value = """
        SELECT e
        FROM exercicios e
        WHERE UPPER(e.grupo_muscular) = UPPER(:grupoMuscular)
    """)
    List<ExerciciosEntity> findAllByGrupoMuscularNative(@Param("grupoMuscular") String grupoMuscular);

}
