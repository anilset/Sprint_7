package ru.practikum;

public class CourierCreds {
    public String login;
    public String password;

    public CourierCreds(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public CourierCreds() {

    }

    public static CourierCreds getCredsFrom(Courier courier) {
        return new CourierCreds(courier.getLogin(), courier.getPassword());
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
