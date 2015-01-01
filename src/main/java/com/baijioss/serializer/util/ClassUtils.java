package com.baijioss.serializer.util;

public final class ClassUtils {

    private ClassUtils() {
    }

    /**
     * Loads a class using the class loader.
     * 1. The class loader of the current class is being used.
     * 2. The thread context class loader is being used.
     * If both approaches fail, returns null.
     *
     * @param className The name of the class to load.
     * @return The class or null if no class loader could load the class.
     */
    public static Class<?> forName(String className)
            throws ClassNotFoundException {
        return ClassUtils.forName(ClassUtils.class, className);
    }

    /**
     * Loads a class using the class loader.
     * 1. The class loader of the context class is being used.
     * 2. The thread context class loader is being used.
     * If both approaches fail, returns null.
     *
     * @param contextClass The name of a context class to use.
     * @param className    The name of the class to load
     * @return The class or null if no class loader could load the class.
     */
    public static Class<?> forName(Class<?> contextClass, String className)
            throws ClassNotFoundException {
        Class<?> c = null;
        if (contextClass.getClassLoader() != null) {
            c = forName(className, contextClass.getClassLoader());
        }
        if (c == null
                && Thread.currentThread().getContextClassLoader() != null) {
            c = forName(className, Thread.currentThread().getContextClassLoader());
        }
        if (c == null) {
            throw new ClassNotFoundException("Failed to load class" + className);
        }
        return c;
    }

    /**
     * Loads a class using the class loader.
     * 1. The class loader of the context class is being used.
     * 2. The thread context class loader is being used.
     * If both approaches fail, returns null.
     *
     * @param classLoader The classloader to use.
     * @param className   The name of the class to load
     * @return The class or null if no class loader could load the class.
     */
    public static Class<?> forName(ClassLoader classLoader, String className)
            throws ClassNotFoundException {
        Class<?> c = null;
        if (classLoader != null) {
            c = forName(className, classLoader);
        }
        if (c == null && Thread.currentThread().getContextClassLoader() != null) {
            c = forName(className, Thread.currentThread().getContextClassLoader());
        }
        if (c == null) {
            throw new ClassNotFoundException("Failed to load class" + className);
        }
        return c;
    }

    /**
     * Loads a {@link Class} from the specified {@link ClassLoader} without
     * throwing {@link ClassNotFoundException}.
     *
     * @param className
     * @param classLoader
     * @return
     */
    private static Class<?> forName(String className, ClassLoader classLoader) {
        Class<?> c = null;
        if (classLoader != null && className != null) {
            try {
                c = Class.forName(className, true, classLoader);
            } catch (ClassNotFoundException e) {
                //Ignore and return null
            }
        }
        return c;
    }
}
