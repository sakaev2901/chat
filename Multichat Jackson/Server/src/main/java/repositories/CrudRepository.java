package repositories;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    void save(T model);
    List<T> findAll();
    Optional<T> findById(ID id);


}
