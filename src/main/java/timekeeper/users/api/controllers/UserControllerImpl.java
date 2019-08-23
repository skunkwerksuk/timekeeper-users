package timekeeper.users.api.controllers;

import static org.springframework.http.HttpStatus.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import timekeeper.users.api.docs.UserControllerDocs;
import timekeeper.users.exceptions.InvalidUserException;
import timekeeper.users.models.User;
import timekeeper.users.services.contracts.UserService;

@RestController
public class UserControllerImpl implements UserControllerDocs {

  @Autowired UserService userService;

  @Override
  public ResponseEntity<User> getUserById(long employeeId) {
    try {
      User userToReturn = userService.getUserById(employeeId);
      return new ResponseEntity<>(userToReturn, OK);
    } catch (InvalidUserException e) {
      throw new ResponseStatusException(NOT_FOUND, e.getLocalizedMessage());
    } catch (Exception e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }

  @Override
  public ResponseEntity<User> getUserByEmail(String emailAddress) {
    try {
      User userToReturn = userService.getUserByEmail(emailAddress);
      return new ResponseEntity<>(userToReturn, OK);
    } catch (InvalidUserException e) {
      throw new ResponseStatusException(NOT_FOUND, e.getLocalizedMessage());
    } catch (Exception e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }

  @Override
  public ResponseEntity<User> getUserByName(String firstName, String lastName) {
    try {
      User userToReturn = userService.getUserByName(firstName, lastName);
      return new ResponseEntity<>(userToReturn, OK);
    } catch (InvalidUserException e) {
      throw new ResponseStatusException(NOT_FOUND, e.getLocalizedMessage());
    } catch (Exception e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }

  @Override
  public ResponseEntity createUser(User userToAdd) {
    try {
      userService.createUser(userToAdd);
      return new ResponseEntity<>(
          "User " + userToAdd.getEmailAddress() + " successfully created.", CREATED);
    } catch (InvalidUserException e) {
      throw new ResponseStatusException(CONFLICT, e.getLocalizedMessage());
    } catch (Exception e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }

  @Override
  public ResponseEntity updateUser(long userId, User userToUpdate) {
    try {
      userService.updateUser(userId, userToUpdate);
      return new ResponseEntity<>("User with userId " + userId + " successfully updated.", OK);
    } catch (InvalidUserException e) {
      throw new ResponseStatusException(NOT_FOUND, e.getLocalizedMessage());
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

  /**
   * This setter method should be used only by unit tests.
   *
   * @param userService
   */
  public void setUserService(UserService userService) {
    this.userService = userService;
  }
}
