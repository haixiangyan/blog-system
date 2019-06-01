package hello.entity;

public class LoginResult extends Result<User> {
    public boolean isLogin;

    protected LoginResult(String status, String msg, User user, boolean isLogin) {
        super(status, msg, user);
        this.isLogin = isLogin;
    }

    public static LoginResult success(String msg, User user) {
        return new LoginResult("OK", msg, user, true);
    }

    public static LoginResult failure(String msg) {
        return new LoginResult("OK", msg, null, false);
    }

    public boolean isLogin() {
        return isLogin;
    }
}
