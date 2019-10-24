package timekeeper.users.services.contracts;

import java.util.List;
import java.util.Optional;
import timekeeper.users.models.User;

public interface UserService {

  User createUser(String firstName, String lastName, String emailAddress, Long approverId);

  User deleteUser(Long userId);

  Optional<User> updateUser(
      Long userId, String firstName, String lastName, String emailAddress, Long approverId);

  Optional<User> getUserById(Long userId);

  Optional<User> getUserByEmail(String emailAddress);

  Optional<User> getUserByName(String firstName, String lastName);

  List<User> getAllUsersByApprover(Long approverId);
}
