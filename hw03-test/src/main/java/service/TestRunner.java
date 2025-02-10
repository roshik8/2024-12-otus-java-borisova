package service;

import annotation.After;
import annotation.Before;
import annotation.Test;
import model.TestResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    public static TestResult run(String className) {
        TestResult result = new TestResult(); // Создаем объект для хранения результатов

        try {
            Class<?> testClass = Class.forName(className);
            Method[] methods = testClass.getDeclaredMethods();

            for (Method testMethod : getMethodsByAnnotation(methods, Test.class)) {
                Object testInstance = testClass.getDeclaredConstructor().newInstance();
                try {
                    runMethods(testInstance, getMethodsByAnnotation(methods, Before.class));
                    runSingleTest(testInstance, testMethod);
                    result.addPassedTest(testMethod.getName());
                } catch (Exception e) {
                    result.addFailedTest(testMethod.getName());
                } finally {
                    runMethods(testInstance, getMethodsByAnnotation(methods, After.class));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static List<Method> getMethodsByAnnotation(Method[] methods, Class<? extends Annotation> annotation) {
        List<Method> result = new ArrayList<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(annotation)) {
                result.add(method);
            }
        }
        return result;
    }

    private static void runMethods(Object instance, List<Method> methods) throws Exception {
        for (Method method : methods) {
            method.setAccessible(true);
            method.invoke(instance);
        }
    }

    private static void runSingleTest(Object instance, Method testMethod) throws Exception {
        testMethod.setAccessible(true);
        testMethod.invoke(instance);
    }
}
