package hello.entity;

import java.time.Instant;

public class User {
    public Integer id;
    public String userName;
    public String encodedPassword;
    public String avatar;
    public Instant createdAt;
    public Instant updatedAt;

    public User(Integer id, String userName, String encodedPassword) {
        this.id = id;
        this.userName = userName;
        this.encodedPassword = encodedPassword;
        this.avatar = "";
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Integer getId() {
        return id;
    }

    public String getuserName() {
        return userName;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public String getAvatar() {
        return avatar;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setuserName(String userName) {
        this.userName = userName;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
