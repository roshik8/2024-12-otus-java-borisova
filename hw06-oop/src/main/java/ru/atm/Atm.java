package ru.atm;

import java.util.Map;

public interface Atm {
    void upBalance(int denomination, int count);
    Map<Integer, Integer> withdrawMoney(int amount);
    int getBalance();
}
