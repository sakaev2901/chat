package models;

public class User {
    private Integer id;
    private String name;
    private AuthData authData;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AuthData getAuthData() {
        return authData;
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
