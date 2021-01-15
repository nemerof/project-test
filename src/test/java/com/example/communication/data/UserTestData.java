package com.example.communication.data;

import com.example.communication.model.Role;
import com.example.communication.model.User;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class UserTestData {
  public static final User ADMIN = new User
      (1001L, "admin", "admin123", true, "admin123@gmail.com", "default-profile-icon.png",
          "Artyom Kosenko", "1990-10-12", "Penza", EnumSet.of(Role.ADMIN));

  public static final User USER = new User
      (1002L, "user", "user123", true, "user123@gmail.com", "default-profile-icon.png",
          "Pasha Alekseev", "1999-02-23", "Cheboksary", EnumSet.of(Role.USER));

  public static final User USR = new User
      (1000L, "usr", "user123", true, "usr123@gmail.com", "default-profile-icon.png",
          "Great Name", "1993-11-01", "Moscow", EnumSet.of(Role.USER));

  public static final List<User> USERS = Arrays.asList(USR, ADMIN, USER);
  public static final User[] USERS_ARRAY = {USR, USER, ADMIN};

  public static User getNew() {
    return new User("New user", "password","new_user@rambler.ru", true, EnumSet.of(Role.USER));
  }

  public static User getUpdated() {
    return new User(USER.getId(), "userUpd", "user123", true, "user123@gmail.com", "default-profile-icon.png",
        "Pasha Alekseev", "1999-02-23", "Cheboksary", EnumSet.of(Role.USER));
  }
}
