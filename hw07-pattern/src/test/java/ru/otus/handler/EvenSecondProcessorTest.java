package ru.otus.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.homework.EvenSecondProcessor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

public class EvenSecondProcessorTest {
    @DisplayName("Тестируем процессор, который выбрасывает исключение в четную секунду")
    @Test
    void evenSecondProcessorTest() {

        var message = new Message.Builder(1L).field3("field3").build();
        var processor1 = new EvenSecondProcessor(() -> LocalDateTime.ofEpochSecond(2, 0, ZoneOffset.ofHours(0)));
        var ex = assertThrows(RuntimeException.class, () -> processor1.process(message));
        assertEquals("1970-01-01T00:00:02", ex.getMessage());
    }

    @DisplayName("Тестируем процессор, который не выбрасывает исключение в нечетную секунду")
    @Test
    void evenSecondNotThrownProcessorTest() {

        var message = new Message.Builder(1L).field3("field3").build();
        var processor1 = new EvenSecondProcessor(() -> LocalDateTime.ofEpochSecond(3, 0, ZoneOffset.ofHours(0)));
        var result = processor1.process(message);
        assertSame(message, result);
    }
}
