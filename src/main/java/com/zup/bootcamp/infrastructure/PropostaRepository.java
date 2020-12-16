package com.zup.bootcamp.infrastructure;

import com.zup.bootcamp.model.Proposta;
import com.zup.bootcamp.model.enums.StatusProposta;
import org.hibernate.LockOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PropostaRepository extends JpaRepository<Proposta, UUID> {

    Optional<Proposta> findByDocumento(String documento);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
            @QueryHint(name = "javax.persistence.lock.timeout", value = (LockOptions.SKIP_LOCKED + ""))
    })
    List<Proposta> findTop10ByStatusAndCartaoNullOrderByInstanteInclusaoAsc(StatusProposta status);

}
