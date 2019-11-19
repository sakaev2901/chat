package net;

public final class Session {
    private Integer id;
    private String role;

    private static Session instance;

    private Session(String role, Integer id) {
        this.role = role;
        this.id = id;
    }

    public static Session getInstance(String role, Integer id) {
        if (instance == null) {
            instance = new Session(role, id);
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
