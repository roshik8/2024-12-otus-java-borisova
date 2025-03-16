package ru.atm;

import java.util.*;

public class SimpleAtm implements Atm {
    private final List<AtmCell> moneyBox;

    public SimpleAtm() {
        moneyBox = new ArrayList<>();
        for (Nominal nominal : Nominal.values()) {
            moneyBox.add(new AtmCell(nominal, 0));
        }
    }

    @Override
    public void upBalance(Nominal nominal, int count) {
        moneyBox.stream()
                .filter(cell -> cell.getNominal() == nominal)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Номинал не поддерживается банкоматом"))
                .addBanknotes(count);
    }

    @Override
    public Map<Nominal, Integer> withdrawMoney(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }

        List<AtmCell> originalBox = new ArrayList<>();
        for (AtmCell cell : moneyBox) {
            originalBox.add(new AtmCell(cell.getNominal(), cell.getCount()));
        }
        Map<Nominal, Integer> withdrawnMoney = new HashMap<>();

        moneyBox.sort(Comparator.comparing((AtmCell cell) -> cell.getNominal().getValue()).reversed());
        for (AtmCell cell : moneyBox) {
            int numNotes = Math.min(amount / cell.getNominal().getValue(), cell.getCount());
            if (numNotes > 0) {
                withdrawnMoney.put(cell.getNominal(), numNotes);
                amount -= cell.getNominal().getValue() * numNotes;
                cell.withdrawBanknotes(numNotes);
            }
        }

        if (amount > 0) {
            moneyBox.clear();
            moneyBox.addAll(originalBox);
            throw new IllegalArgumentException("Невозможно выдать запрошенную сумму");
        }

        return withdrawnMoney;
    }

    @Override
    public int getBalance() {
        return moneyBox.stream().mapToInt(cell -> cell.getNominal().getValue() * cell.getCount()).sum();
    }

    @Override
    public String toString() {
        return String.format("Текущий баланс: %d %s", getBalance(), moneyBox);
    }
}
