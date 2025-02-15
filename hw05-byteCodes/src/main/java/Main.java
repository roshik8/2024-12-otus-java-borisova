import proxy.Ioc;
import service.TestLogging;
import service.TestLoggingInterface;

public class Main {
    public static void main(String[] args) {
        TestLoggingInterface myClass = Ioc.createProxy(new TestLogging(), TestLoggingInterface.class);
        myClass.calculation(0);
        myClass.calculation(2, 4);
        myClass.calculation(2, 5, "ляляля");
    }
}
