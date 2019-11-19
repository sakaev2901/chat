package net;

public final class Session {
    private Integer id;
    private String role;

    private static Session instance;

    private Session(String role) {
        this.role = role;
    }

    public static Session getInstance(String role) {
        if (instance == null) {
            instance = new Session(role);
        }
        return instance;
    }

    public static Session getInstance() {
        return instance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
