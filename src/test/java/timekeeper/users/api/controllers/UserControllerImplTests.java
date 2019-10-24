package timekeeper.users.api.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static timekeeper.users.utils.TestUtils.getDefaultUser;
import static timekeeper.users.utils.TestUtils.getListOfUsers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;
import timekeeper.users.exceptions.InvalidUserException;
import timekeeper.users.models.User;
import timekeeper.users.services.contracts.UserService;
import timekeeper.users.services.impls.UserServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerImplTests {

  @Autowired private UserControllerImpl controller;

  private UserService mockUserService;

  @Before
  public void setUp() {
    controller = new UserControllerImpl();
    mockUserService = mock(UserServiceImpl.class);
    controller.setUserService(mockUserService);
  }

  @Test
  public void getUserById_Successful() {
    User expectedUser = getDefaultUser();
    ResponseEntity<User> expectedResponse = new ResponseEntity<>(expectedUser, HttpStatus.OK);
    when(mockUserService.getUserById(expectedUser.getUserId()))
        .thenReturn(Optional.of(expectedUser));

    ResponseEntity<User> actualResponse = controller.getUserById(expectedUser.getUserId());

    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  public void getUserById_NotFound() {
    long userId = 12345;
    when(mockUserService.getUserById(userId)).thenReturn(Optional.empty());

    ResponseEntity actual = controller.getUserById(userId);

    assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
  }

  @Test(expected = ResponseStatusException.class)
  public void getUserById_InternalServerError() {
    long userId = 12345;
    when(mockUserService.getUserById(userId)).thenThrow(new RuntimeException("something broke"));

    ResponseEntity actual = controller.getUserById(userId);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    assertEquals("something broke", Objects.requireNonNull(actual.getBody()).toString());
  }

  @Test
  public void getUserByEmail_Successful() {
    User expectedUser = getDefaultUser();
    ResponseEntity<User> expectedResponse = new ResponseEntity<>(expectedUser, HttpStatus.OK);
    when(mockUserService.getUserByEmail(expectedUser.getEmailAddress()))
        .thenReturn(Optional.of(expectedUser));

    ResponseEntity<User> actualResponse = controller.getUserByEmail(expectedUser.getEmailAddress());

    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  public void getUserByEmail_NotFound() {
    String email = "test@email.com";
    when(mockUserService.getUserByEmail(email)).thenReturn(Optional.empty());

    ResponseEntity actual = controller.getUserByEmail(email);

    assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
  }

  @Test(expected = ResponseStatusException.class)
  public void getUserByEmail_InternalServerError() {
    String email = "test@email.com";
    when(mockUserService.getUserByEmail(email)).thenThrow(new RuntimeException("something broke"));

    ResponseEntity actual = controller.getUserByEmail(email);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    assertEquals("something broke", Objects.requireNonNull(actual.getBody()).toString());
  }

  @Test
  public void getUserByName_Successful() {
    User expectedUser = getDefaultUser();
    ResponseEntity<User> expectedResponse = new ResponseEntity<>(expectedUser, HttpStatus.OK);
    when(mockUserService.getUserByName(expectedUser.getFirstName(), expectedUser.getLastName()))
        .thenReturn(Optional.of(expectedUser));

    ResponseEntity<User> actualResponse =
        controller.getUserByName(expectedUser.getFirstName(), expectedUser.getLastName());

    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  public void getUserByName_NotFound() {
    String firstName = "John";
    String lastName = "Doe";
    when(mockUserService.getUserByName(firstName, lastName)).thenReturn(Optional.empty());

    ResponseEntity actual = controller.getUserByName(firstName, lastName);

    assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
  }

  @Test(expected = ResponseStatusException.class)
  public void getUserByName_InternalServerError() {
    String firstName = "John";
    String lastName = "Doe";
    when(mockUserService.getUserByName(firstName, lastName))
        .thenThrow(new RuntimeException("something broke"));

    ResponseEntity actual = controller.getUserByName(firstName, lastName);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    assertEquals("something broke", Objects.requireNonNull(actual.getBody()).toString());
  }

  public void getUsersByApproverId_Successful() {
    List<User> expectedUsers = getListOfUsers();
    long approverId = 1234;
    ResponseEntity<List<User>> expectedResponse =
        new ResponseEntity<>(expectedUsers, HttpStatus.OK);
    when(mockUserService.getAllUsersByApprover(approverId)).thenReturn(expectedUsers);

    ResponseEntity actualResponse = controller.getUsersByApprover(approverId);

    assertEquals(expectedResponse, actualResponse);
  }

  @Test(expected = ResponseStatusException.class)
  public void getUsersByApproverId_ApproverNotFound() {
    long approverId = 12345;
    when(mockUserService.getAllUsersByApprover(approverId))
        .thenThrow(new InvalidUserException("approver not found"));

    ResponseEntity actual = controller.getUsersByApprover(approverId);

    assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    assertEquals("approver not found", Objects.requireNonNull(actual.getBody()).toString());
  }

  @Test(expected = ResponseStatusException.class)
  public void getUsersByApproverId_InternalServerError() {
    long approverId = 12345;
    when(mockUserService.getAllUsersByApprover(approverId))
        .thenThrow(new RuntimeException("something broke"));

    ResponseEntity actual = controller.getUsersByApprover(approverId);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    assertEquals("something broke", Objects.requireNonNull(actual.getBody()).toString());
  }

  @Test
  public void createUser_Successful() {
    User userToCreate = getDefaultUser();
    ResponseEntity expectedResponse =
        new ResponseEntity<>("User john.doe@email.com successfully created.", HttpStatus.CREATED);
    when(mockUserService.createUser(
            userToCreate.getFirstName(),
            userToCreate.getLastName(),
            userToCreate.getEmailAddress(),
            userToCreate.getApproverId()))
        .thenReturn(userToCreate);

    ResponseEntity actualResponse =
        controller.createUser(
            userToCreate.getFirstName(),
            userToCreate.getLastName(),
            userToCreate.getEmailAddress(),
            userToCreate.getApproverId());

    assertEquals(expectedResponse, actualResponse);
  }

  @Test(expected = ResponseStatusException.class)
  public void createUser_AlreadyExists() {
    User userToCreate = getDefaultUser();
    when(mockUserService.createUser(
            userToCreate.getFirstName(),
            userToCreate.getLastName(),
            userToCreate.getEmailAddress(),
            userToCreate.getApproverId()))
        .thenThrow(new InvalidUserException("user already exists"));

    ResponseEntity actualResponse =
        controller.createUser(
            userToCreate.getFirstName(),
            userToCreate.getLastName(),
            userToCreate.getEmailAddress(),
            userToCreate.getApproverId());

    assertEquals(HttpStatus.CONFLICT, actualResponse.getStatusCode());
    assertEquals(
        "user already exists", Objects.requireNonNull(actualResponse.getBody()).toString());
  }

  @Test(expected = ResponseStatusException.class)
  public void createUser_InternalServerError() {
    User userToCreate = getDefaultUser();
    when(mockUserService.createUser(
            userToCreate.getFirstName(),
            userToCreate.getLastName(),
            userToCreate.getEmailAddress(),
            userToCreate.getApproverId()))
        .thenThrow(new RuntimeException("something broke"));

    ResponseEntity actual =
        controller.createUser(
            userToCreate.getFirstName(),
            userToCreate.getLastName(),
            userToCreate.getEmailAddress(),
            userToCreate.getApproverId());

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    assertEquals("something broke", Objects.requireNonNull(actual.getBody()).toString());
  }

  @Test
  public void updateUser_Successful() {
    User userToUpdate = getDefaultUser();
    ResponseEntity expectedResponse =
        new ResponseEntity<>("User with userId 123 successfully updated.", HttpStatus.OK);
    when(mockUserService.updateUser(userToUpdate.getUserId(), userToUpdate))
        .thenReturn(userToUpdate);

    ResponseEntity actualResponse = controller.updateUser(userToUpdate.getUserId(), userToUpdate);

    assertEquals(expectedResponse, actualResponse);
  }

  @Test(expected = ResponseStatusException.class)
  public void updateUser_NotFound() {
    User userToUpdate = getDefaultUser();
    when(mockUserService.updateUser(userToUpdate.getUserId(), userToUpdate))
        .thenThrow(new InvalidUserException("user not found"));

    ResponseEntity actualResponse = controller.updateUser(userToUpdate.getUserId(), userToUpdate);

    assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
    assertEquals("user not found", Objects.requireNonNull(actualResponse.getBody()).toString());
  }

  @Test(expected = ResponseStatusException.class)
  public void updateUser_InternalServerError() {
    User userToUpdate = getDefaultUser();
    when(mockUserService.updateUser(userToUpdate.getUserId(), userToUpdate))
        .thenThrow(new RuntimeException("something broke"));

    ResponseEntity actual = controller.updateUser(userToUpdate.getUserId(), userToUpdate);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    assertEquals("something broke", Objects.requireNonNull(actual.getBody()).toString());
  }

  @Test
  public void deleteUser_Successful() {
    long userIdToDelete = 1234;
    ResponseEntity expectedResponse =
        new ResponseEntity<>("User with userId 1234 successfully deleted.", HttpStatus.OK);
    when(mockUserService.deleteUser(userIdToDelete)).thenReturn(getDefaultUser());

    ResponseEntity actualResponse = controller.deleteUser(userIdToDelete);

    assertEquals(expectedResponse, actualResponse);
  }

  @Test(expected = ResponseStatusException.class)
  public void deleteUser_NotFound() {
    long userIdToDelete = 1234;
    when(mockUserService.deleteUser(userIdToDelete))
        .thenThrow(new InvalidUserException("user not found"));

    ResponseEntity actualResponse = controller.deleteUser(userIdToDelete);

    assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
    assertEquals("user not found", Objects.requireNonNull(actualResponse.getBody()).toString());
  }

  @Test(expected = ResponseStatusException.class)
  public void deleteUser_InternalServerError() {
    long userIdToDelete = 1234;
    when(mockUserService.deleteUser(userIdToDelete))
        .thenThrow(new RuntimeException("something broke"));

    ResponseEntity actual = controller.deleteUser(userIdToDelete);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    assertEquals("something broke", Objects.requireNonNull(actual.getBody()).toString());
  }
}
