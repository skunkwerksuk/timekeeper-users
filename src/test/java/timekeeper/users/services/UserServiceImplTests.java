package timekeeper.users.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static timekeeper.users.utils.TestUtils.getDefaultUser;
import static timekeeper.users.utils.TestUtils.getListOfUsers;

import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import timekeeper.users.exceptions.InvalidUserException;
import timekeeper.users.models.User;
import timekeeper.users.repositories.UserRepository;
import timekeeper.users.services.impls.UserServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTests {

  private UserServiceImpl userService;

  private UserRepository mockUserRepository;

  @Before
  public void setUp() {
    userService = new UserServiceImpl();

    mockUserRepository = mock(UserRepository.class);
    userService.setUserRepository(mockUserRepository);
  }

  @Test
  public void registerUser_successful() {
    final User expectedUser = getDefaultUser();

    when(mockUserRepository.findById(expectedUser.getEmployeeId())).thenReturn(Optional.empty());
    when(mockUserRepository.save(any(User.class))).thenReturn(expectedUser);

    final User returnedUser = userService.createUser(expectedUser);

    verify(mockUserRepository, times(1)).save(any(User.class));
    assertEquals(expectedUser, returnedUser);
  }

  @Test(expected = InvalidUserException.class)
  public void registerUser_alreadyExists() {
    final User expectedUser = getDefaultUser();
    when(mockUserRepository.findById(expectedUser.getEmployeeId()))
        .thenReturn(Optional.of(expectedUser));

    userService.createUser(expectedUser);

    verify(mockUserRepository, times(1)).findById(expectedUser.getEmployeeId());
    verifyNoMoreInteractions(mockUserRepository);
  }

  @Test
  public void deleteUser_successful() {
    User toBeDeleted = getDefaultUser();
    Long toBeDeletedEmployeeId = toBeDeleted.getEmployeeId();
    when(mockUserRepository.findById(toBeDeletedEmployeeId)).thenReturn(Optional.of(toBeDeleted));

    User deleted = userService.deleteUser(toBeDeletedEmployeeId);

    verify(mockUserRepository, times(1)).findById(toBeDeletedEmployeeId);
    verify(mockUserRepository, times(1)).deleteById(toBeDeletedEmployeeId);
    verifyNoMoreInteractions(mockUserRepository);

    assertEquals(toBeDeleted, deleted);
  }

  @Test(expected = InvalidUserException.class)
  public void deleteUser_notFound() {
    long id = 1234;
    when(mockUserRepository.findById(id)).thenReturn(Optional.empty());
    userService.deleteUser(id);

    verify(mockUserRepository, times(1)).findById(id);
    verifyNoMoreInteractions(mockUserRepository);
  }

  @Test
  public void updateUser_successful() {
    User originalUser = getDefaultUser();
    User toBeUpdated = getDefaultUser();
    toBeUpdated.setFirstName("Thomas");

    when(mockUserRepository.findById(toBeUpdated.getEmployeeId()))
        .thenReturn(Optional.of(originalUser));
    when(mockUserRepository.save(toBeUpdated)).thenReturn(toBeUpdated);

    User updatedUser = userService.updateUser(originalUser.getEmployeeId(), toBeUpdated);

    verify(mockUserRepository, times(1)).findById(toBeUpdated.getEmployeeId());
    verify(mockUserRepository, times(1)).save(any(User.class));
    verifyNoMoreInteractions(mockUserRepository);

    assertNotEquals(originalUser.getFirstName(), updatedUser.getFirstName());
    assertEquals("Thomas", updatedUser.getFirstName());
  }

  @Test(expected = InvalidUserException.class)
  public void updateUser_notFound() {
    User toBeUpdated = getDefaultUser();

    when(mockUserRepository.findById(toBeUpdated.getEmployeeId())).thenReturn(Optional.empty());

    userService.updateUser(toBeUpdated.getEmployeeId(), toBeUpdated);

    verify(mockUserRepository, times(1)).findById(toBeUpdated.getEmployeeId());
    verifyNoMoreInteractions(mockUserRepository);
  }

  @Test(expected = InvalidUserException.class)
  public void updateUser_differentIds() {
    User toBeUpdated = getDefaultUser();
    User originalUser = getDefaultUser();
    originalUser.setEmployeeId((long) 54321);

    when(mockUserRepository.findById(originalUser.getEmployeeId()))
        .thenReturn(Optional.of(originalUser));

    userService.updateUser(toBeUpdated.getEmployeeId(), toBeUpdated);

    verify(mockUserRepository, times(1)).findById(originalUser.getEmployeeId());
    verifyNoMoreInteractions(mockUserRepository);
  }

  @Test
  public void findUserById_successful() {
    Optional<User> expectedUser = Optional.of(getDefaultUser());
    when(mockUserRepository.findById(expectedUser.get().getEmployeeId())).thenReturn(expectedUser);
    Optional<User> actual = userService.getUserById(expectedUser.get().getEmployeeId());

    assertEquals(expectedUser, actual);
  }

  @Test
  public void findUserById_notFound() {
    User user = getDefaultUser();
    when(mockUserRepository.findById(user.getEmployeeId())).thenReturn(Optional.empty());
    Optional<User> actualUser = userService.getUserById(user.getEmployeeId());

    assertEquals(Optional.empty(), actualUser);
  }

  @Test
  public void findUserByEmail_successful() {
    Optional<User> expectedUser = Optional.of(getDefaultUser());
    when(mockUserRepository.findUserByEmailAddress(expectedUser.get().getEmailAddress()))
        .thenReturn(expectedUser);
    Optional<User> actual = userService.getUserByEmail(expectedUser.get().getEmailAddress());

    assertEquals(expectedUser, actual);
  }

  @Test
  public void findUserByEmail_notFound() {
    User user = getDefaultUser();
    when(mockUserRepository.findUserByEmailAddress(user.getEmailAddress()))
        .thenReturn(Optional.empty());
    Optional<User> actualUser = userService.getUserByEmail(user.getEmailAddress());

    assertEquals(Optional.empty(), actualUser);
  }

  @Test
  public void findUserByName_successful() {
    User expectedUser = getDefaultUser();
    when(mockUserRepository.findUserByFirstNameAndLastName(
            expectedUser.getFirstName(), expectedUser.getLastName()))
        .thenReturn(Optional.of(expectedUser));

    Optional<User> actual =
        userService.getUserByName(expectedUser.getFirstName(), expectedUser.getLastName());

    assertEquals(Optional.of(expectedUser), actual);
  }

  @Test
  public void findUserByName_notFound() {
    User user = getDefaultUser();
    when(mockUserRepository.findUserByFirstNameAndLastName(user.getFirstName(), user.getLastName()))
        .thenReturn(Optional.empty());

    Optional<User> actualUser = userService.getUserByName(user.getFirstName(), user.getLastName());

    assertEquals(Optional.empty(), actualUser);
  }

  @Test
  public void findAllUsersWithApprover_successful() {
    User approver = getDefaultUser();
    List<User> expectedUsers = getListOfUsers();
    when(mockUserRepository.findById(approver.getEmployeeId())).thenReturn(Optional.of(approver));
    when(mockUserRepository.findAllByApproverId(approver.getEmployeeId()))
        .thenReturn(Optional.of(getListOfUsers()));

    List<User> actualUsers = userService.getAllUsersByApprover(approver.getEmployeeId());

    assertEquals(expectedUsers, actualUsers);
  }

  @Test(expected = InvalidUserException.class)
  public void findAllUsersWithApprover_approverNotFound() {
    User approver = getDefaultUser();
    when(mockUserRepository.findById(approver.getEmployeeId())).thenReturn(Optional.empty());
    userService.getAllUsersByApprover(approver.getEmployeeId());
  }
}
