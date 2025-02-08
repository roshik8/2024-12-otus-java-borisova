import annotation.After;
import annotation.Before;
import annotation.Test;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    public static void run(String className) {
        try {
            Class<?> testClass = Class.forName(className);
            Method[] methods = testClass.getDeclaredMethods();

            int passed = 0, failed = 0;
            List<String> passedTests = new ArrayList<>();
            List<String> failedTests = new ArrayList<>();

            for (Method testMethod : getMethodsByAnnotation(methods, Test.class)) {
                Object testInstance = testClass.getDeclaredConstructor().newInstance();
                try {
                    runMethods(testInstance, getMethodsByAnnotation(methods, Before.class));
                    runSingleTest(testInstance, testMethod);
                    passedTests.add(testMethod.getName());
                    passed++;
                } catch (Exception e) {
                    failedTests.add(testMethod.getName());
                    failed++;
                } finally {
                    runMethods(testInstance, getMethodsByAnnotation(methods, After.class));
                }
            }
            System.out.println("Результаты:");
            System.out.printf("Пройдено тестов %d, названия тестов %s%n", passed, passedTests);

            System.out.printf("Провалено тестов %d, названия тестов %s%n", failed, failedTests);
            System.out.println("Всего: " + (passed + failed));

        } catch (Exception e) {
            e.printStackTrace();
        }
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
