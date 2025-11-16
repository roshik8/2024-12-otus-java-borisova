package ru.otus.numbers;

import io.grpc.ServerBuilder;
import java.io.IOException;
import ru.otus.numbers.service.NumbersServiceImpl;

@SuppressWarnings({"squid:S106"})
public class GRPCServer {

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {
        var server = ServerBuilder.forPort(SERVER_PORT)
                .addService(new NumbersServiceImpl())
                .build();
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        System.out.println("жду запросов от клиента...");
        server.awaitTermination();
    }
}