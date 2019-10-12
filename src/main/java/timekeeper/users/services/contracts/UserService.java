package timekeeper.users.services.contracts;

import java.util.List;
import java.util.Optional;
import timekeeper.users.models.User;

public interface UserService {

  User createUser(User user);

  User deleteUser(Long userId);

  User updateUser(Long userId, User user);

  Optional<User> getUserById(Long userId);

  Optional<User> getUserByEmail(String emailAddress);

  Optional<User> getUserByName(String firstName, String lastName);

  List<User> getAllUsersByApprover(Long approverId);
}
