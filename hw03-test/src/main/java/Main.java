import model.TestResult;
import service.TestRunner;

public class Main {
    public static void main(String[] args) {

        TestResult result = TestRunner.run("test.AnnotationTest");
        System.out.println(result);
    }
}
