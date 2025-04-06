package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.ZoneId;

public class EvenSecondProcessor implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public EvenSecondProcessor(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if (dateTimeProvider.getDate().atZone(ZoneId.systemDefault()).toEpochSecond() % 2 == 0) {
            throw new RuntimeException(dateTimeProvider.getDate().toString());
        }
        return message;
    }
}
