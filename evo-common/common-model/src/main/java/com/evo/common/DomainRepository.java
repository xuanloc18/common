package com.evo.common;

import java.util.List;
import java.util.Optional;

public interface DomainRepository<D,ID> {
    Optional<D> findById(ID id);
    List<D> findAllByIds(List<ID> ids);
    D save(D domain);
    boolean saveAll(List<D> domains);
    boolean delete(D domain);
    boolean deleteById(ID id);
    boolean existsById(ID id);
}
