package com.zup.bootcamp.infrastructure;

import com.zup.bootcamp.model.Bloqueio;
import com.zup.bootcamp.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BloqueioRepository extends JpaRepository<Bloqueio, Long> {
    Optional<Bloqueio> findByCartao(Cartao cartao);
}
