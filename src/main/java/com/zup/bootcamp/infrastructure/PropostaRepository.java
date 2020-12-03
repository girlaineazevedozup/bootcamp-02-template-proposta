package com.zup.bootcamp.infrastructure;

import com.zup.bootcamp.model.Proposta;
import com.zup.bootcamp.model.StatusProposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    Optional<Proposta> findByDocumento(String documento);

    List<Proposta> findByStatusAndNumeroCartaoNull(StatusProposta status);

}
