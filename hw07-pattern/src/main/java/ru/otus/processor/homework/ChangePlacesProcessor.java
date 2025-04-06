package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ChangePlacesProcessor implements Processor {
    @Override
    public Message process(Message message) {
        String field11 = message.getField11();
        return message.toBuilder().field11(message.getField12()).field12(field11).build();
    }
}
