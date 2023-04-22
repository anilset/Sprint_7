package ru.practikum.pojo;

public class Courier {
    public String login;
    public String password;
    public String firstName;

    public String getLogin() {
        return login;
    }

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public Courier() {}

    public Courier setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Courier setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Courier setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }
}

