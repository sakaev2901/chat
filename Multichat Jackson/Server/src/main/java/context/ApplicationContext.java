package context;

public interface ApplicationContext {
    void scan(Object demo) throws ClassNotFoundException;
    <T> T getComponent(Class<T> componentType, String name);
}
