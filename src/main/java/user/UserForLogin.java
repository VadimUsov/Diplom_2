package user;

public class UserForLogin {

    private String email;
    private String password;

    public UserForLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserForLogin() {
    }

    public static UserForLogin from(User user) {
        return new UserForLogin(user.getEmail(), user.getPassword());
    }

    public static UserForLogin withWrongPassword(User user) {
        return new UserForLogin(user.getEmail(), user.getPassword() + "wrong");
    }

    public static UserForLogin withWrongEmail(User user) {
        return new UserForLogin(user.getEmail() + "wrong", user.getPassword());
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String name) {
        this.email = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

