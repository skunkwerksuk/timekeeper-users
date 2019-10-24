package timekeeper.users.services.impls;

import java.util.List;
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
  public User createUser(String firstName, String lastName, String emailAddress, Long approverId) {
    Optional<User> existingUser = getUserByEmail(emailAddress);
    if (existingUser.isPresent())
      throw new InvalidUserException(
          "User already exists with id: " + existingUser.get().getUserId());
    return userRepository.save(new User(0L, firstName, lastName, emailAddress, approverId));
  }

  @Transactional(rollbackFor = InvalidUserException.class)
  @Override
  public User deleteUser(Long userId) throws InvalidUserException {
    Optional<User> toBeDeleted = userRepository.findById(userId);
    if (!toBeDeleted.isPresent())
      throw new InvalidUserException("No user found with id: " + userId);
    userRepository.deleteById(userId);
    return toBeDeleted.get();
  }

  @Transactional(rollbackFor = InvalidUserException.class)
  @Override
  public User updateUser(Long userId, User user) {
    Optional<User> toBeUpdated = userRepository.findById(userId);
    if (!toBeUpdated.isPresent())
      throw new InvalidUserException("No user found with id: " + userId);
    if (!user.getUserId().equals(toBeUpdated.get().getUserId()))
      throw new InvalidUserException("userId's do not match");
    return userRepository.save(user);
  }

  @Transactional(readOnly = true)
  @Override
  public Optional<User> getUserById(Long userId) {
    return userRepository.findById(userId);
  }

  @Transactional(readOnly = true)
  @Override
  public Optional<User> getUserByEmail(String emailAddress) {
    return userRepository.findUserByEmailAddress(emailAddress);
  }

  @Transactional(readOnly = true)
  @Override
  public Optional<User> getUserByName(String firstName, String lastName) {
    return userRepository.findUserByFirstNameAndLastName(firstName, lastName);
  }

  @Transactional(readOnly = true)
  @Override
  public List<User> getAllUsersByApprover(Long approverId) {
    Optional<User> approver = userRepository.findById(approverId);
    if (!approver.isPresent())
      throw new InvalidUserException("No approver found with id: " + approverId);
    Optional<List<User>> userList = userRepository.findAllByApproverId(approverId);
    if (!userList.isPresent())
      throw new InvalidUserException("No users found for the approver with id: " + approverId);
    return userList.get();
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
