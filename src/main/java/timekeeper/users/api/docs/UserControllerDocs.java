package timekeeper.users.api.docs;

import io.swagger.annotations.*;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import timekeeper.users.models.User;

@Api(value = "User API", description = "Endpoints allowing CRUD operations on the user table")
public interface UserControllerDocs {
  @ApiOperation(value = "Get a user by userId", response = User.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "No user found with id: {userId}"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @GetMapping("/get-user-by-id")
  ResponseEntity<User> getUserById(
      @ApiParam(value = "The id of the user", required = true) long userId);

  @ApiOperation(value = "Get a user by their email address", response = User.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "No user found with email address: {emailAddress}"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @GetMapping("/get-user-by-email")
  ResponseEntity<User> getUserByEmail(
      @ApiParam(
              value = "The email address of the user you want to get the details of",
              required = true)
          String emailAddress);

  @ApiOperation(value = "Get a user by their first name and last name", response = User.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "No user found with name: {firstName} {lastName}"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @GetMapping("/get-user-by-name")
  ResponseEntity<User> getUserByName(
      @ApiParam(value = "The first name of the user", required = true) String firstName,
      @ApiParam(value = "The last name of the user", required = true) String lastName);

  @ApiOperation(value = "Get a all users who have the specified approver", response = List.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "No approver found with id: {approverId}"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @GetMapping("/get-users-by-approver")
  ResponseEntity getUsersByApprover(
      @ApiParam(
              value = "The id of the approver that you want to get the users for",
              required = true)
          long approverId);

  @ApiOperation(value = "Add a new user to the database")
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "User {emailAddress} successfully created."),
        @ApiResponse(code = 409, message = "User already exists with id: {userId}"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @PostMapping("/create-user")
  ResponseEntity createUser(
      @ApiParam(value = "The first name of the new user", required = true) String firstName,
      @ApiParam(value = "The last name of the new user", required = true) String lastName,
      @ApiParam(value = "The email address of the new user", required = true) String emailAddress,
      @ApiParam(value = "The id of the user who approves the new users absences", required = true)
          Long approverId);

  @ApiOperation(value = "Update a user in the database")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "No user found with id: {userId}"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @PutMapping("/update-user")
  ResponseEntity updateUser(
      @ApiParam(value = "The id of the user to be updated", required = true) Long userId,
      @ApiParam(value = "The first name of the user to be updated", required = true)
          String firstName,
      @ApiParam(value = "The last name of the user to be updated", required = true) String lastName,
      @ApiParam(value = "The email address of the user to be updated", required = true)
          String emailAddress,
      @ApiParam(value = "The id of the user who approves the users absences", required = true)
          Long approverId);

  @ApiOperation(value = "Delete a user from the database")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "No user found with id: {userId}"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @DeleteMapping("/delete-user")
  ResponseEntity deleteUser(
      @ApiParam(value = "The id of the user to be deleted", required = true) long userId);
}
