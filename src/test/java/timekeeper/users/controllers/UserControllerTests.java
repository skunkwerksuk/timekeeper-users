package timekeeper.users.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static timekeeper.users.utils.TestUtils.getDefaultUser;

import java.util.Objects;
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
public class UserControllerTests {

  @Autowired private UserController controller;

  private UserService mockUserService;

  @Before
  public void setUp() {
    controller = new UserController();
    mockUserService = mock(UserServiceImpl.class);
    controller.setUserService(mockUserService);
  }

  @Test
  public void getUserById_Successful() {
    User expectedUser = getDefaultUser();
    ResponseEntity<User> expectedResponse = new ResponseEntity<>(expectedUser, HttpStatus.OK);
    when(mockUserService.getUserById(expectedUser.getEmployeeId())).thenReturn(expectedUser);

    ResponseEntity<User> actualResponse = controller.getUser(expectedUser.getEmployeeId());

    assertEquals(expectedResponse, actualResponse);
  }

  @Test(expected = ResponseStatusException.class)
  public void getUserById_NotFound() {
    long employeeId = 12345;
    when(mockUserService.getUserById(employeeId))
        .thenThrow(new InvalidUserException("user not found"));

    ResponseEntity actual = controller.getUser(employeeId);

    assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    assertEquals("user not found", Objects.requireNonNull(actual.getBody()).toString());
  }

  @Test(expected = ResponseStatusException.class)
  public void getUserById_InternalServerError() {
    long employeeId = 12345;
    when(mockUserService.getUserById(employeeId))
        .thenThrow(new RuntimeException("something broke"));

    ResponseEntity actual = controller.getUser(employeeId);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    assertEquals("something broke", Objects.requireNonNull(actual.getBody()).toString());
  }

  @Test
  public void getUserByEmail_Successful() {
    User expectedUser = getDefaultUser();
    ResponseEntity<User> expectedResponse = new ResponseEntity<>(expectedUser, HttpStatus.OK);
    when(mockUserService.getUserByEmail(expectedUser.getEmailAddress())).thenReturn(expectedUser);

    ResponseEntity<User> actualResponse = controller.getUserByEmail(expectedUser.getEmailAddress());

    assertEquals(expectedResponse, actualResponse);
  }

  @Test(expected = ResponseStatusException.class)
  public void getUserByEmail_NotFound() {
    String email = "test@email.com";
    when(mockUserService.getUserByEmail(email))
        .thenThrow(new InvalidUserException("user not found"));

    ResponseEntity actual = controller.getUserByEmail(email);

    assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    assertEquals("user not found", Objects.requireNonNull(actual.getBody()).toString());
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
        .thenReturn(expectedUser);

    ResponseEntity<User> actualResponse =
        controller.getUserByName(expectedUser.getFirstName(), expectedUser.getLastName());

    assertEquals(expectedResponse, actualResponse);
  }

  @Test(expected = ResponseStatusException.class)
  public void getUserByName_NotFound() {
    String firstName = "John";
    String lastName = "Doe";
    when(mockUserService.getUserByName(firstName, lastName))
        .thenThrow(new InvalidUserException("user not found"));

    ResponseEntity actual = controller.getUserByName(firstName, lastName);

    assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    assertEquals("user not found", Objects.requireNonNull(actual.getBody()).toString());
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

  @Test
  public void createUser_Successful() {
    User userToCreate = getDefaultUser();
    ResponseEntity expectedResponse =
        new ResponseEntity<>("User john.doe@email.com successfully created.", HttpStatus.CREATED);
    when(mockUserService.createUser(userToCreate)).thenReturn(userToCreate);

    ResponseEntity actualResponse = controller.createUser(userToCreate);

    assertEquals(expectedResponse, actualResponse);
  }

  @Test(expected = ResponseStatusException.class)
  public void createUser_AlreadyExists() {
    User userToCreate = getDefaultUser();
    when(mockUserService.createUser(userToCreate))
        .thenThrow(new InvalidUserException("user already exists"));

    ResponseEntity actualResponse = controller.createUser(userToCreate);

    assertEquals(HttpStatus.CONFLICT, actualResponse.getStatusCode());
    assertEquals(
        "user already exists", Objects.requireNonNull(actualResponse.getBody()).toString());
  }

  @Test(expected = ResponseStatusException.class)
  public void createUser_InternalServerError() {
    User userToCreate = getDefaultUser();
    when(mockUserService.createUser(userToCreate))
        .thenThrow(new RuntimeException("something broke"));

    ResponseEntity actual = controller.createUser(userToCreate);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    assertEquals("something broke", Objects.requireNonNull(actual.getBody()).toString());
  }

  @Test
  public void updateUser_Successful() {
    User userToUpdate = getDefaultUser();
    ResponseEntity expectedResponse =
        new ResponseEntity<>("User with userId 123 successfully updated.", HttpStatus.OK);
    when(mockUserService.updateUser(userToUpdate.getEmployeeId(), userToUpdate))
        .thenReturn(userToUpdate);

    ResponseEntity actualResponse =
        controller.updateUser(userToUpdate.getEmployeeId(), userToUpdate);

    assertEquals(expectedResponse, actualResponse);
  }

  @Test(expected = ResponseStatusException.class)
  public void updateUser_NotFound() {
    User userToUpdate = getDefaultUser();
    when(mockUserService.updateUser(userToUpdate.getEmployeeId(), userToUpdate))
        .thenThrow(new InvalidUserException("user not found"));

    ResponseEntity actualResponse =
        controller.updateUser(userToUpdate.getEmployeeId(), userToUpdate);

    assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
    assertEquals("user not found", Objects.requireNonNull(actualResponse.getBody()).toString());
  }

  @Test(expected = ResponseStatusException.class)
  public void updateUser_InternalServerError() {
    User userToUpdate = getDefaultUser();
    when(mockUserService.updateUser(userToUpdate.getEmployeeId(), userToUpdate))
        .thenThrow(new RuntimeException("something broke"));

    ResponseEntity actual = controller.updateUser(userToUpdate.getEmployeeId(), userToUpdate);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    assertEquals("something broke", Objects.requireNonNull(actual.getBody()).toString());
  }
}
