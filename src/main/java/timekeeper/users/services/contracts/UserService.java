package timekeeper.users.services.contracts;

import timekeeper.users.models.User;

public interface UserService {

  User registerUser(User user);

  User deleteUser(Long employeeId);

  User updateUser(Long employeeId, User user);

  User getUserById(Long employeeId);

  User getUserByEmail(String emailAddress);

  User getUserByName(String firstName, String lastName);

  User getAllUsersByApprover(Long approverId);
}
