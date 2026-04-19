package br.com.souza.spring_boot_essentials.database.repository;

import br.com.souza.spring_boot_essentials.database.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<RoleEntity, Integer> {

    Optional<RoleEntity> findByNome(String nome);
}
