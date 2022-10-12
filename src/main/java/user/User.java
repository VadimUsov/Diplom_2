package user;

import org.apache.commons.lang3.RandomStringUtils;

public class User {

    private String email;

    private String password;

    private String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User() {

    }

    public static User getRandomUser() {
        return new User(
                RandomStringUtils.randomAlphabetic(10).toLowerCase() + "@diplomtest.ru",
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphabetic(10)
        );
    }

    public static User getUserWithoutEmail() {
        return new User(
                "",
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphabetic(10)
        );
    }

    public static User getUserWithoutPassword() {
        return new User(
                RandomStringUtils.randomAlphabetic(10) + "@diplomtest.ru",
                "",
                RandomStringUtils.randomAlphabetic(10)
        );
    }

    public static User getUserWithoutName() {
        return new User(
                RandomStringUtils.randomAlphabetic(10) + "@diplomtest.ru",
                RandomStringUtils.randomAlphanumeric(10),
                ""
        );
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }
}

