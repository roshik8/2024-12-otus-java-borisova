package ru.atm;

import java.util.*;

public class SimpleAtm implements Atm {
    private final TreeMap<Integer, Integer> moneyBox;

    public SimpleAtm(List<Integer> denominations) {
        moneyBox = new TreeMap<>(Collections.reverseOrder());
        for (int d : denominations) {
            moneyBox.put(d, 0);
        }
    }

    @Override
    public void upBalance(int denomination, int count) {
        if (!moneyBox.containsKey(denomination)) {
            throw new IllegalArgumentException(String.format("Банкомат не принимает купюры номиналом %d", denomination));
        }
        if (count <= 0) {
            throw new IllegalArgumentException("Количество банкнот должно быть положительным");
        }
        moneyBox.put(denomination, moneyBox.get(denomination) + count);
    }

    @Override
    public Map<Integer, Integer> withdrawMoney(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }

        Map<Integer, Integer> originalBox = new TreeMap<>(moneyBox);
        Map<Integer, Integer> withdrawnMoney = new HashMap<>();

        for (int denomination : moneyBox.keySet()) {
            int numNotes = Math.min(amount / denomination, moneyBox.get(denomination));
            if (numNotes > 0) {
                withdrawnMoney.put(denomination, numNotes);
                amount -= denomination * numNotes;
                moneyBox.put(denomination, moneyBox.get(denomination) - numNotes);
            }
        }

        if (amount > 0) {
            moneyBox.clear();
            moneyBox.putAll(originalBox);
            throw new IllegalArgumentException("Невозможно выдать запрошенную сумму");
        }

        return withdrawnMoney;
    }

    @Override
    public int getBalance() {
        return moneyBox.entrySet().stream().mapToInt(e -> e.getKey() * e.getValue()).sum();
    }

    @Override
    public String toString() {
        return String.format("Текущий баланс: %d %s", getBalance(), moneyBox);
    }
}
