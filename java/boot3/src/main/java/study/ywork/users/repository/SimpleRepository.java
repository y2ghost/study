package study.ywork.users.repository;

import java.util.Optional;

public interface SimpleRepository<T, ID> {
    T save(T domain);

    Optional<T> findById(ID id);

    Iterable<T> findAll();

    void deleteById(ID id);
}
