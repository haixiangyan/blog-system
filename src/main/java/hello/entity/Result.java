package hello.entity;

public abstract class Result<T> {
    public String status;
    public String msg;
    public T data;

    protected Result(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    protected Result(String status, String msg, T data) {
        this(status, msg);
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }
}
