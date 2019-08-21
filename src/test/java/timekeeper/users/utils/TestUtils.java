package timekeeper.users.utils;

import java.util.ArrayList;
import java.util.List;
import timekeeper.users.models.User;

public class TestUtils {

  public static User getDefaultUser() {
    return new User((long) 123, "John", "Doe", "john.doe@email.com", (long) 1234);
  }

  public static List<User> getListOfUsers() {
    List<User> userList = new ArrayList<User>();
    User johnDoe = new User((long) 123, "John", "Doe", "john.doe@email.com", (long) 1234);
    User janeDoe = new User((long) 124, "Jane", "Doe", "jane.doe@email.com", (long) 1234);
    userList.add(johnDoe);
    userList.add(janeDoe);
    return userList;
  }
}
