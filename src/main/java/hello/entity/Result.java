package hello.entity;

public class Result {
    public String status;
    public String msg;
    public boolean isLogin;
    public Object data;

    public static Result failure(String message) {
        return new Result("Fail", message, false);
    }

    public static Result success(String message, User loggedInUser) {
        return new Result("Success", message, true, loggedInUser);
    }

    public Result(String status, String msg, boolean isLogin) {
        this.status = status;
        this.msg = msg;
        this.isLogin = isLogin;
    }

    public Result(String status, String msg, boolean isLogin, Object data) {
        this(status, msg, isLogin);
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public Object getData() {
        return data;
    }
}
