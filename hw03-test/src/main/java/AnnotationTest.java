import annotation.After;
import annotation.Before;
import annotation.Test;

public class AnnotationTest {
    @Test
    void testMethod1() {
        System.out.println("testMethod1");
    }

    @Before
    void setUp() {
        System.out.println("setUp");
    }

    @Before
    void beforeMethod() {
        System.out.println("beforeMethod");
    }

    @After
    void afterMethod() {
        System.out.println("afterMethod");
    }

    @Test
    void testMethod2() {
        System.out.println("testMethod2");
        throw new RuntimeException("Я все сломаю");
    }
}
