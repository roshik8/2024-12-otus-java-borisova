package ru.otus.numbers.service;

import io.grpc.stub.StreamObserver;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import ru.otus.numbers.NumbersRequest;
import ru.otus.numbers.NumbersResponse;
import ru.otus.numbers.NumbersServiceGrpc;

public class NumbersServiceImpl extends NumbersServiceGrpc.NumbersServiceImplBase {

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

    @Override
    public void streamNumber(NumbersRequest request, StreamObserver<NumbersResponse> responseObserver) {

        var currentValue = new AtomicLong(request.getFirstValue());

        Runnable task = () -> {
            var value = currentValue.incrementAndGet();
            var response = NumbersResponse.newBuilder().setValue(value).build();
            responseObserver.onNext(response);
            System.out.println("отправляю: " + value);
            if (value == request.getLastValue()) {
                executor.shutdown();
                responseObserver.onCompleted();
                System.out.println("больше ничего не отправляю, клиент сказал хватит");
            }
        };
        executor.scheduleAtFixedRate(task, 0, 2, TimeUnit.SECONDS);
    }
}
