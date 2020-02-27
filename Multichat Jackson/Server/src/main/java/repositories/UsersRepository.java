package repositories;

import models.User;

import java.util.Optional;

public interface UsersRepository extends CrudRepository<User, Integer> {
    Optional<User> findByMailAndPassword(String mail, String password);
    String findVerifierById(Integer id);
    void updateVerifier(String verifier, Integer id);
}
