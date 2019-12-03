package repositories;

import models.User;

public interface UsersRepository extends CrudRepository<User, Integer> {
    User findByMailAndPassword(String mail, String password);
    String findVerifierById(Integer id);
    void updateVerifier(String verifier, Integer id);
}
