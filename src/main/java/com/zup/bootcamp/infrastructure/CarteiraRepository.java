package com.zup.bootcamp.infrastructure;

import com.zup.bootcamp.model.Cartao;
import com.zup.bootcamp.model.Carteira;
import com.zup.bootcamp.model.enums.TipoCarteira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarteiraRepository extends JpaRepository<Carteira, Long> {
    Optional<Carteira> findByCartaoAndCarteira(Cartao cartao, TipoCarteira carteira);

}
