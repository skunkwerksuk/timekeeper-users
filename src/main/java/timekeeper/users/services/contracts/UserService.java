package timekeeper.users.services.contracts;

import timekeeper.users.models.User;

public interface UserService {

  void registerUser(User user);

  void deleteUser(Long employeeId);

  User updateUser(Long employeeId, User user);

  User getUserById(Long employeeId);

  User getUserByEmail(String emailAddress);

  User getUserByName(String firstName, String lastName);

  void makeUserApprover(Long employeeId, Long approverId);
}
