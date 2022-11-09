package ru.job4j.dreamjob.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime created;

    public User() {
    }

    public User(int id, String name, String email, String password, LocalDateTime created) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id
                + ", name='" + name
                + '\'' + ", email='"
                + email + '\'' + ", password='"
                + password + '\'' + ", created="
                + created + '}';
    }
}
