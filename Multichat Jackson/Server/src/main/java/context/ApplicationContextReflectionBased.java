package context;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ApplicationContextReflectionBased implements ApplicationContext {

    @Override
    public void scan(Object demo) {
        Class tClass = demo.getClass();
        Field[] fields = tClass.getDeclaredFields();
        for (Field field:
                fields) {
            Class fieldClass = field.getType();
            Class<?> fieldImpl = null;
            try {
                String classToString = fieldClass.toString();
                if (!classToString.split(" ")[0].equals("class")) {
                    fieldImpl = Class.forName(fieldClass.getTypeName() + "Impl");
                } else  {
                    fieldImpl = Class.forName(fieldClass.getTypeName());
                }
                try {
                    fieldImpl.getMethod("getComponentName");
                    String s = "set" + field.getType().getTypeName().split("\\.")[1];
                    System.out.println(s);
                    Method method = tClass.getMethod(s, fieldImpl);
                    try {
                        Object o = fieldImpl.newInstance();
                        method.invoke(demo, o);
                        scan(o);
                        System.out.println(3);
                    } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        throw new IllegalStateException(e);
                    }
                } catch (NoSuchMethodException e) {
                    //ignore
                    System.out.println("..");
                }
            } catch (ClassNotFoundException e) {
                //ignore
            }

        }
    }

   public  <T> T getComponent(Class<T> componentType, String name) {
        try {
            T model = componentType.newInstance();
            scan(model);
            return model;
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
