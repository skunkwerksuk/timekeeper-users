package timekeeper.users.utils;

import timekeeper.users.models.User;

public class TestUtils {

  public static User getDefaultUser() {
    return new User((long) 123, "John", "Doe", "john.doe@email.com", (long) 1234);
  }
}
