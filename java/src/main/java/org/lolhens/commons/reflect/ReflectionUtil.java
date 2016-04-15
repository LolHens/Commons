package org.lolhens.commons.reflect;

import org.lolhens.commons.primitive.PrimitiveUtil;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class ReflectionUtil {

    public static Set<Field> getAnnotatedFields(Class<?> target, List<Class<? extends Annotation>> annotations, Class<?> reqType) {
        Set<Field> fields = new HashSet<>();
        if (reqType == void.class) return fields;

        for (Field field : getAllDeclaredFields(target)) {
            if (annotations != null) {
                boolean annotationPresent = false;
                for (Class<? extends Annotation> annotation : annotations)
                    if (field.isAnnotationPresent(annotation)) {
                        annotationPresent = true;
                        break;
                    }
                if (!annotationPresent) continue;
            }

            if (reqType != null && field.getType() != reqType) continue;
            fields.add(field);
        }
        return fields;
    }

    public static Set<Field> getAnnotatedFields(Class<?> target, Class<? extends Annotation> annotation, Class<?> reqType) {
        return getAnnotatedFields(target, Arrays.asList(annotation), reqType);
    }

    public static Set<Method> getAnnotatedMethods(Class<?> target, List<Class<? extends Annotation>> annotations, Class<?> reqType, List<Class<?>> reqArgs) {
        Set<Method> methods = new HashSet<>();

        for (Method method : getAllDeclaredMethods(target)) {
            if (annotations != null) {
                boolean annotationPresent = false;
                for (Class<? extends Annotation> annotation : annotations)
                    if (method.isAnnotationPresent(annotation)) {
                        annotationPresent = true;
                        break;
                    }
                if (!annotationPresent) continue;
            }

            if (reqType != null && method.getReturnType() != reqType) continue;
            if (reqArgs != null && !reqArgs.equals(Arrays.asList(method.getParameterTypes()))) continue;
            methods.add(method);
        }
        return methods;
    }

    public static Set<Method> getAnnotatedMethods(Class<?> target, Class<? extends Annotation> annotation, Class<?> reqType, List<Class<?>> reqArgs) {
        return getAnnotatedMethods(target, Arrays.asList(annotation), reqType, reqArgs);
    }

    public static Set<Constructor<?>> getAnnotatedConstructors(Class<?> target, List<Class<? extends Annotation>> annotations, List<Class<?>> reqArgs) {
        Set<Constructor<?>> constructors = new HashSet<>();

        for (Constructor<?> constructor : getAllDeclaredConstructors(target)) {
            if (annotations != null) {
                boolean annotationPresent = false;
                for (Class<? extends Annotation> annotation : annotations)
                    if (constructor.isAnnotationPresent(annotation)) {
                        annotationPresent = true;
                        break;
                    }
                if (!annotationPresent) continue;
            }

            if (reqArgs != null && !reqArgs.equals(Arrays.asList(constructor.getParameterTypes()))) continue;
            constructors.add(constructor);
        }
        return constructors;
    }

    public static Set<Constructor<?>> getAnnotatedConstructors(Class<?> target, Class<? extends Annotation> annotation, List<Class<?>> reqArgs) {
        return getAnnotatedConstructors(target, Arrays.asList(annotation), reqArgs);
    }

    public static Set<Field> getAllDeclaredFields(Class<?> target) {
        Set<Field> fields = new HashSet<>();
        getAllDeclaredFields(target, fields);
        return fields;
    }

    private static void getAllDeclaredFields(Class<?> target, Set<Field> fields) {
        try {
            for (Field field : target.getDeclaredFields()) {
                if (!field.isAccessible()) field.setAccessible(true);
                fields.add(field);
            }
        } catch (NoClassDefFoundError e) {
            throw new RuntimeException(e);
        }
        Class<?> superclass = target.getSuperclass();
        if (superclass != null) getAllDeclaredFields(superclass, fields);
    }

    public static Set<Method> getAllDeclaredMethods(Class<?> target) {
        Set<Method> methods = new HashSet<>();
        getAllDeclaredMethods(target, methods);
        return methods;
    }

    private static void getAllDeclaredMethods(Class<?> target, Set<Method> methods) {
        try {
            for (Method method : target.getDeclaredMethods()) {
                if (!method.isAccessible()) method.setAccessible(true);
                methods.add(method);
            }
        } catch (NoClassDefFoundError e) {
            throw new RuntimeException(e);
        }
        Class<?> superclass = target.getSuperclass();
        if (superclass != null) getAllDeclaredMethods(superclass, methods);
    }

    public static Set<Constructor<?>> getAllDeclaredConstructors(Class<?> target) {
        Set<Constructor<?>> constructors = new HashSet<>();
        getAllDeclaredConstructors(target, constructors);
        return constructors;
    }

    private static void getAllDeclaredConstructors(Class<?> target, Set<Constructor<?>> constructors) {
        try {
            for (Constructor<?> constructor : target.getDeclaredConstructors()) {
                if (!constructor.isAccessible()) constructor.setAccessible(true);
                constructors.add(constructor);
            }
        } catch (NoClassDefFoundError e) {
            throw new RuntimeException(e);
        }
        Class<?> superclass = target.getSuperclass();
        if (superclass != null) getAllDeclaredConstructors(superclass, constructors);
    }

    /**
     * Used to get an array of null objects (or false for boolean, 0 for
     * numbers, etc.) for dynamic instantiation. You can also insert objects for
     * specific types instead of null.
     *
     * @param argTypes    Class&lt;?&gt;[] - the argument Types
     * @param appliedArgs Object... - The arg types that are replaced with objects
     *                    Example: ([Class&lt;?&gt;[] argTypes], String.class, "test")
     * @return Object[] - Array with null or objects (look above)
     */
    public static Object[] buildArgumentArray(Class<?>[] argTypes, Object... appliedArgs) {
        Object[] args = new Object[argTypes.length];
        for (int i1 = 0; i1 < args.length; i1++) {
            for (int i2 = 0; i2 + 1 < appliedArgs.length; i2 += 2) {
                if (argTypes[i1] == appliedArgs[i2]) {
                    args[i1] = appliedArgs[i2 + 1];
                    break;
                }
            }
            if (args[i1] == null && argTypes[i1].isPrimitive()) args[i1] = PrimitiveUtil.get(argTypes[i1]).nullObj;

        }
        return args;
    }

    public static Constructor<?> getConstructor(Class<?> target) {
        try {
            Constructor<?> constructor = target.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static <E> E newInstance(Class<E> clazz) {
        if (Object.class.isAssignableFrom(clazz)) {
            Exception lastException = null;
            Constructor<E>[] constructors = (Constructor<E>[]) clazz.getDeclaredConstructors();
            for (Constructor<E> constructor : constructors) {
                constructor.setAccessible(true);
                try {
                    return constructor.newInstance(buildArgumentArray(constructor.getParameterTypes()));
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    lastException = e;
                }
            }
            if (lastException != null)
                throw new RuntimeException(lastException);

            return null;
        } else
            return (E) PrimitiveUtil.get(clazz).nullObj;
    }

    @Target({METHOD, FIELD})
    @Retention(RUNTIME)
    @Documented
    public @interface SingletonInstance {
    }

    @SuppressWarnings("unchecked")
    public static <ClassType> ClassType getSingletonInstance(Class<?> target, Object... appliedArgs) {
        for (Field field : getAnnotatedFields(target, SingletonInstance.class, null)) {
            if (!Modifier.isStatic(field.getModifiers())) continue;
            try {
                return (ClassType) field.get(null);
            } catch (Exception e) {
            }
        }
        for (Method method : getAnnotatedMethods(target, SingletonInstance.class, null, null)) {
            if (!Modifier.isStatic(method.getModifiers())) continue;
            try {
                return (ClassType) method.invoke(null, buildArgumentArray(method.getParameterTypes(), appliedArgs));
            } catch (Exception e) {
            }
        }
        return null;
    }
}
