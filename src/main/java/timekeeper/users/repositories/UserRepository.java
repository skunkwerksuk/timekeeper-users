package timekeeper.users.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timekeeper.users.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findUserByFirstNameAndLastName(String firstName, String lastName);

  Optional<User> findUserByEmailAddress(String emailAddress);

  Optional<List<User>> findAllByApproverId(Long approverId);
}
