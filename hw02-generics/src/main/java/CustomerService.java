import java.util.*;

public class CustomerService {

    private final TreeMap<Customer, String> customers = new TreeMap<>(Comparator.comparingLong(Customer::getScores)) {};
    // todo: 3. надо реализовать методы этого класса
    // важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    public Map.Entry<Customer, String> getSmallest() {
        var smallest = customers.firstEntry();
        return smallest == null
                ? null
                : Map.entry(
                        new Customer(
                                smallest.getKey().getId(),
                                smallest.getKey().getName(),
                                smallest.getKey().getScores()),
                        smallest.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        var highest = customers.higherEntry(customer);
        return highest == null
                ? null
                : Map.entry(
                        new Customer(
                                highest.getKey().getId(),
                                highest.getKey().getName(),
                                highest.getKey().getScores()),
                        highest.getValue());
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }
}
