package service;

import annotation.Log;

public class TestLogging implements TestLoggingInterface {

    @Override
    @Log
    public void calculation(int param1) {
        System.out.printf("calculation(%d)%n", param1);
    }

    @Override
    public void calculation(int param1, int param2) {
        System.out.printf("calculation(%d, %d)%n", param1, param2);
    }

    @Override
    @Log
    public void calculation(int param1, int param2, String param3) {
        System.out.printf("Executing calculation(%d, %d, %s)%n", param1, param2, param3);
    }
}
