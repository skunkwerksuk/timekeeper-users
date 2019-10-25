package timekeeper.users.api.controllers;

import static org.springframework.http.HttpStatus.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import timekeeper.users.api.docs.UserControllerDocs;
import timekeeper.users.exceptions.InvalidUserException;
import timekeeper.users.models.User;
import timekeeper.users.services.contracts.UserService;

@RestController
public class UserControllerImpl implements UserControllerDocs {

  private final UserService userService;

  @Autowired
  public UserControllerImpl(UserService userService) {
    this.userService = userService;
  }

  @Override
  public ResponseEntity<User> getUserById(long userId) {
    try {
      return userService
          .getUserById(userId)
          .map(user -> new ResponseEntity<>(user, OK))
          .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    } catch (Exception e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }

  @Override
  public ResponseEntity<User> getUserByEmail(String emailAddress) {
    try {
      return userService
          .getUserByEmail(emailAddress)
          .map(user -> new ResponseEntity<>(user, OK))
          .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    } catch (Exception e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }

  @Override
  public ResponseEntity<User> getUserByName(String firstName, String lastName) {
    try {
      return userService
          .getUserByName(firstName, lastName)
          .map(user -> new ResponseEntity<>(user, OK))
          .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    } catch (Exception e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }

  @Override
  public ResponseEntity createUser(
      String firstName, String lastName, String emailAddress, Long approverId) {
    try {
      userService.createUser(firstName, lastName, emailAddress, approverId);
      return new ResponseEntity<>("User " + emailAddress + " successfully created.", CREATED);
    } catch (InvalidUserException e) {
      throw new ResponseStatusException(CONFLICT, e.getLocalizedMessage());
    } catch (Exception e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }

  @Override
  public ResponseEntity updateUser(
      Long userId, String firstName, String lastName, String emailAddress, Long approverId) {
    try {
      return userService
          .updateUser(userId, firstName, lastName, emailAddress, approverId)
          .map(absence -> new ResponseEntity<>(absence, OK))
          .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    } catch (Exception e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }

  @Override
  public ResponseEntity deleteUser(long userId) {
    try {
      userService.deleteUser(userId);
      return new ResponseEntity<>("User with userId " + userId + " successfully deleted.", OK);
    } catch (InvalidUserException e) {
      throw new ResponseStatusException(NOT_FOUND, e.getLocalizedMessage());
    } catch (Exception e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }

  @Override
  public ResponseEntity getUsersByApprover(long approverId) {
    try {
      List<User> usersList = userService.getAllUsersByApprover(approverId);
      return new ResponseEntity<>(usersList, OK);
    } catch (InvalidUserException e) {
      throw new ResponseStatusException(NOT_FOUND, e.getLocalizedMessage());
    } catch (Exception e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }
}
