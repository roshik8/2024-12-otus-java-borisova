package ru.atm;

public class AtmCell {
    private final Nominal nominal;
    private int count;

    public AtmCell(Nominal nominal, int count) {
        this.nominal = nominal;
        this.count = count;
    }

    public Nominal getNominal() {
        return nominal;
    }

    public int getCount() {
        return count;
    }

    public void addBanknotes(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Количество банкнот должно быть положительным");
        }
        this.count += count;
    }

    public boolean withdrawBanknotes(int count) {
        if (count > this.count) {
            return false;
        }
        this.count -= count;
        return true;
    }

    @Override
    public String toString() {
        return String.format("Ячейка: номинал = %d, количество = %d", nominal.getValue(), count);
    }
}
