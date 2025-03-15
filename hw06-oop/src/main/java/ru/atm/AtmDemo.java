package ru.atm;


import java.util.Arrays;

public class AtmDemo {

    public static void main(String[] args) {
        Atm atm = new SimpleAtm(Arrays.asList(100, 500, 1000, 5000));
        atm.upBalance(1000, 5);
        atm.upBalance(500, 10);
        System.out.println(atm);

        try {
            System.out.printf("Выдача %d: %s%n", 4500, atm.withdrawMoney(4500));
        } catch (IllegalArgumentException e) {
            System.out.printf("Ошибка: %s%n", e.getMessage());
        }
        System.out.println(atm);
    }
}
