package timekeeper.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timekeeper.users.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findUserByFirstNameAndLastName(String firstName, String lastName);

  User findUserByEmailAddress(String emailAddress);
}
