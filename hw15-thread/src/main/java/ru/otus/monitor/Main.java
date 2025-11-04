package ru.otus.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static boolean first = true;
    private int current = 1;
    private int step = 1;
    private boolean finished = false;

    private synchronized void action(String name, boolean firstThread) {
        while (!Thread.currentThread().isInterrupted() && !finished) {
            try {
                while (first != firstThread) {
                    this.wait();
                }
                logger.info("{}: {}", name, current);
                if (!first && current == 1 && step == -1) {
                    finished = true;
                    notifyAll();
                    return;
                }
                first = !first;
                if (first) {
                    if (current == 10) step = -1;
                    else if (current == 1) step = 1;
                    current += step;
                }
                notifyAll();
                sleep();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        new Thread(() -> main.action("Поток 1", true)).start();
        new Thread(() -> main.action("Поток 2", false)).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}