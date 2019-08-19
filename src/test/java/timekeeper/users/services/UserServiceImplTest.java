package timekeeper.users.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
import timekeeper.users.utils.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

  private UserServiceImpl userService;

  private UserRepository mockUserRepository;

  @Before
  public void setUp() {
    userService = new UserServiceImpl();

    mockUserRepository = mock(UserRepository.class);
    userService.setUserRepository(mockUserRepository);
  }

  @Test
  public void registerUser_Successful() {
    final User expectedUser = TestUtils.getDefaultUser();

    when(mockUserRepository.save(any(User.class))).thenReturn(expectedUser);

    final User returnedUser = userService.registerUser(expectedUser);

    verify(mockUserRepository, times(1)).save(any(User.class));
    assertEquals(expectedUser, returnedUser);
  }

  @Test
  public void deleteUser_Successful() {
    User toBeDeleted = TestUtils.getDefaultUser();
    Long toBeDeletedEmployeeId = toBeDeleted.getEmployeeId();
    when(mockUserRepository.findById(toBeDeletedEmployeeId)).thenReturn(Optional.of(toBeDeleted));

    User deleted = userService.deleteUser(toBeDeletedEmployeeId);

    verify(mockUserRepository, times(1)).findById(toBeDeletedEmployeeId);
    verify(mockUserRepository, times(1)).deleteById(toBeDeletedEmployeeId);
    verifyNoMoreInteractions(mockUserRepository);

    assertEquals(toBeDeleted, deleted);
  }

  @Test(expected = InvalidUserException.class)
  public void deleteUser_NotFound() {
    long id = 1234;
    when(mockUserRepository.findById(id)).thenReturn(Optional.empty());
    userService.deleteUser(id);

    verify(mockUserRepository, times(1)).findById(id);
    verifyNoMoreInteractions(mockUserRepository);
  }

  @Test
  public void updateUser_Successful() {
    User originalUser = TestUtils.getDefaultUser();
    User toBeUpdated = TestUtils.getDefaultUser();
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
  public void updateUser_NotFound() {
    User toBeUpdated = TestUtils.getDefaultUser();

    when(mockUserRepository.findById(toBeUpdated.getEmployeeId())).thenReturn(Optional.empty());

    userService.updateUser(toBeUpdated.getEmployeeId(), toBeUpdated);

    verify(mockUserRepository, times(1)).findById(toBeUpdated.getEmployeeId());
    verifyNoMoreInteractions(mockUserRepository);
  }

  @Test(expected = InvalidUserException.class)
  public void updateUser_DifferentIds() {
    User toBeUpdated = TestUtils.getDefaultUser();
    User originalUser = TestUtils.getDefaultUser();
    originalUser.setEmployeeId((long) 54321);

    when(mockUserRepository.findById(originalUser.getEmployeeId()))
        .thenReturn(Optional.of(originalUser));

    userService.updateUser(toBeUpdated.getEmployeeId(), toBeUpdated);

    verify(mockUserRepository, times(1)).findById(originalUser.getEmployeeId());
    verifyNoMoreInteractions(mockUserRepository);
  }

  @Test
  public void findUserById_Successful() {
    User expectedUser = TestUtils.getDefaultUser();
    when(mockUserRepository.findById(expectedUser.getEmployeeId()))
        .thenReturn(Optional.of(expectedUser));
    User actual = userService.getUserById(expectedUser.getEmployeeId());
    assertEquals(expectedUser, actual);
  }

  @Test(expected = InvalidUserException.class)
  public void findUserById_NotFound() {
    User user = TestUtils.getDefaultUser();
    when(mockUserRepository.findById(user.getEmployeeId())).thenReturn(Optional.empty());
    userService.getUserById(user.getEmployeeId());
  }
}
