package ru.otus.numbers;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings({"squid:S106"})
public class GRPCClient {

    private static final String HOST = "localhost";
    private static final int PORT = 8190;
    private static final int FIRST_VALUE = 0;
    private static final int LAST_VALUE = 30;
    private static final int COUNT = 50;

    public static void main(String[] args) throws InterruptedException {

        System.out.println("работа с числами началась...");

        var channel =
                ManagedChannelBuilder.forAddress(HOST, PORT).usePlaintext().build();

        var asyncStub = NumbersServiceGrpc.newStub(channel);

        var latch = new CountDownLatch(1);
        AtomicLong lastServerValue = new AtomicLong(0);
        asyncStub.streamNumber(
                NumbersRequest.newBuilder().setFirstValue(FIRST_VALUE).setLastValue(LAST_VALUE).build(),
                new StreamObserver<NumbersResponse>() {
                    @Override
                    public void onNext(NumbersResponse value) {
                        long v = value.getValue();
                        lastServerValue.set(v);
                        System.out.println("новое значение: " + v);                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        latch.countDown();
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("запрос отработал");
                        latch.countDown();
                    }
                });

        long currentValue = 0;
        for (int i = 0; i < COUNT; i++) {
            currentValue = currentValue + lastServerValue.getAndSet(0) + 1;
            System.out.println("текущее значение: " + currentValue);
            Thread.sleep(1000);
        }
        latch.await();

        System.out.println("работа с числами закончилась...");
        channel.shutdown();
    }
}