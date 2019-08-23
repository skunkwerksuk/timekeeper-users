package timekeeper.users.controllers;

import static org.springframework.http.HttpStatus.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import timekeeper.users.exceptions.InvalidUserException;
import timekeeper.users.models.User;
import timekeeper.users.services.contracts.UserService;

@RestController
public class UserController {

  @Autowired UserService userService;

  @GetMapping("/get-user-by-id")
  public ResponseEntity<User> getUser(@RequestParam long employeeId) {
    try {
      User userToReturn = userService.getUserById(employeeId);
      return new ResponseEntity<>(userToReturn, OK);
    } catch (InvalidUserException e) {
      throw new ResponseStatusException(NOT_FOUND, e.getLocalizedMessage());
    } catch (Exception e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }

  @GetMapping("/get-user-by-email")
  public ResponseEntity<User> getUserByEmail(@RequestParam String emailAddress) {
    try {
      User userToReturn = userService.getUserByEmail(emailAddress);
      return new ResponseEntity<>(userToReturn, OK);
    } catch (InvalidUserException e) {
      throw new ResponseStatusException(NOT_FOUND, e.getLocalizedMessage());
    } catch (Exception e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }

  @GetMapping("/get-user-by-name")
  public ResponseEntity<User> getUserByName(
      @RequestParam String firstName, @RequestParam String lastName) {
    try {
      User userToReturn = userService.getUserByName(firstName, lastName);
      return new ResponseEntity<>(userToReturn, OK);
    } catch (InvalidUserException e) {
      throw new ResponseStatusException(NOT_FOUND, e.getLocalizedMessage());
    } catch (Exception e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }

  @PostMapping("/create-user")
  public ResponseEntity createUser(@RequestParam User userToAdd) {
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

  @PutMapping("/update-user")
  public ResponseEntity updateUser(@RequestParam long userId, @RequestParam User userToUpdate) {
    try {
      userService.updateUser(userId, userToUpdate);
      return new ResponseEntity<>("User with userId " + userId + " successfully updated.", OK);
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
