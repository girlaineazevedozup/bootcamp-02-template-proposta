package com.zup.bootcamp.infrastructure;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.function.Supplier;

@Component
public class ExecutorTransacao {

    @PersistenceContext
    private EntityManager manager;

    @Transactional
    public <T> T salvaEComita(T objeto) {
        manager.persist(objeto);
        return objeto;
    }

    @Transactional
    public <T> T atualizaEComita(T objeto) {
        return manager.merge(objeto);
    }

    @Transactional
    public <T> T executa(Supplier<T> algumCodigoComRetorno){
        return algumCodigoComRetorno.get();
    }
}
