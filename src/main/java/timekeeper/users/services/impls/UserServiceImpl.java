package timekeeper.users.services.impls;

import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timekeeper.users.exceptions.InvalidUserException;
import timekeeper.users.models.User;
import timekeeper.users.repositories.UserRepository;
import timekeeper.users.services.contracts.UserService;

@Service
public class UserServiceImpl implements UserService {

  @Autowired @Resource UserRepository userRepository;

  @Transactional
  @Override
  public User registerUser(User user) {
    return userRepository.save(user);
  }

  @Transactional(rollbackFor = InvalidUserException.class)
  @Override
  public User deleteUser(Long employeeId) throws InvalidUserException {
    Optional<User> toBeDeleted = userRepository.findById(employeeId);
    if (!toBeDeleted.isPresent())
      throw new InvalidUserException("No user found with id: " + employeeId);
    userRepository.deleteById(employeeId);
    return toBeDeleted.get();
  }

  @Transactional(rollbackFor = InvalidUserException.class)
  @Override
  public User updateUser(Long employeeId, User user) {
    Optional<User> toBeUpdated = userRepository.findById(employeeId);
    if (!toBeUpdated.isPresent())
      throw new InvalidUserException("No user found with id: " + employeeId);
    if (!user.getEmployeeId().equals(toBeUpdated.get().getEmployeeId()))
      throw new InvalidUserException("employeeId's do not match");
    return userRepository.save(user);
  }

  @Override
  public User getUserById(Long employeeId) {
    Optional<User> user = userRepository.findById(employeeId);
    if (!user.isPresent())
      throw new InvalidUserException("No user found with id: " + employeeId);
    return user.get();
  }

  @Override
  public User getUserByEmail(String emailAddress) {
    return null;
  }

  @Override
  public User getUserByName(String firstName, String lastName) {
    return null;
  }

  /**
   * This setter method should be used only by unit tests.
   *
   * @param userRepository
   */
  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
}
