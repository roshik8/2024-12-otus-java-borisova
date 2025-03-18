package ru.atm;

import java.util.Map;

public interface Atm {
    void upBalance(Nominal denomination, int count);
    Map<Nominal, Integer> withdrawMoney(int amount);
    int getBalance();
}
