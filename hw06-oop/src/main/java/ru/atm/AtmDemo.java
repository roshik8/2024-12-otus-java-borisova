package ru.atm;


public class AtmDemo {

    public static void main(String[] args) {
        Atm atm = new SimpleAtm();
        atm.upBalance(Nominal.ONE_THOUSAND, 5);
        atm.upBalance(Nominal.FIVE_HUNDRED, 10);
        System.out.println(atm);

        try {
            System.out.printf("Выдача %d: %s%n", 4500, atm.withdrawMoney(4500));
        } catch (IllegalArgumentException e) {
            System.out.printf("Ошибка: %s%n", e.getMessage());
        }
        System.out.println(atm);
    }
}
