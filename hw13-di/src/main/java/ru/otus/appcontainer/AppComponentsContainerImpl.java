package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings({"squid:S1068", "unchecked"})
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Object configInstance = createConfigInstance(configClass);
        List<Method> componentMethods = getSortedComponentMethods(configClass);
        createComponents(configInstance, componentMethods);
    }

    private Object createConfigInstance(Class<?> configClass) {
        try {
            return configClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании конструктора класса : " + configClass.getName(), e);
        }
    }

    private List<Method> getSortedComponentMethods(Class<?> configClass) {
        return Arrays.stream(configClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()))
                .toList();
    }

    private void createComponents(Object configInstance, List<Method> componentMethods) {
        for (Method method : componentMethods) {
            AppComponent annotation = method.getAnnotation(AppComponent.class);
            String componentName = annotation.name();
            Object component = createComponent(configInstance, method);
            registerComponent(componentName, component);
        }
    }

    private Object createComponent(Object configInstance, Method method) {
        try {
            Object[] args = Arrays.stream(method.getParameterTypes())
                    .map(this::getAppComponent)
                    .toArray();
            return method.invoke(configInstance, args);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании компонента из метода : " + method.getName(), e);
        }
    }

    private void registerComponent(String componentName, Object component) {
        if (appComponentsByName.containsKey(componentName)) {
            throw new IllegalArgumentException("Дубликат компонента с именем: " + componentName);
        }
        appComponents.add(component);
        appComponentsByName.put(componentName, component);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> components = appComponents.stream()
                .filter(c -> componentClass.isAssignableFrom(c.getClass()))
                .toList();
        if (components.size() != 1) {
            throw new IllegalArgumentException("Ожидается только один компонент  " + componentClass + ", но найдено " + components.size());
        }
        return (C) components.getFirst();
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Object component = appComponentsByName.get(componentName);
        if (component == null) {
            throw new IllegalArgumentException("Не найдено компонента по имени " + componentName);
        }
        return (C) component;
    }
}
