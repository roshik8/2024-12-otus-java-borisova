package ru.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AtmTest {
private Atm atm;

    @BeforeEach
    void setUp() {
      atm = new SimpleAtm();
    }

    @Test
    void testDepositAndBalance() {
        atm.upBalance(Nominal.ONE_THOUSAND, 5);
        atm.upBalance(Nominal.FIVE_HUNDRED, 10);
        assertEquals(10000, atm.getBalance());
    }

    @Test
    void testWithdrawValidAmount() {
        atm.upBalance(Nominal.ONE_THOUSAND, 5);
        atm.upBalance(Nominal.FIVE_HUNDRED, 10);
        Map<Nominal, Integer> withdrawn = atm.withdrawMoney(4500);
        assertEquals(Map.of(Nominal.ONE_THOUSAND, 4, Nominal.FIVE_HUNDRED, 1), withdrawn);
        assertEquals(5500, atm.getBalance());
    }

    @Test
    void testWithdrawInvalidAmount() {
        atm.upBalance(Nominal.ONE_THOUSAND, 2);
        atm.upBalance(Nominal.FIVE_HUNDRED, 1);
        assertThrows(IllegalArgumentException.class, () -> atm.withdrawMoney(300));
    }

    @Test
    void testWithdrawMoreThanAvailable() {
        atm.upBalance(Nominal.ONE_THOUSAND, 2);
        atm.upBalance(Nominal.FIVE_HUNDRED, 1);
        assertThrows(IllegalArgumentException.class, () -> atm.withdrawMoney(5000));
    }

    @Test
    void testDepositInvalidDenomination() {
        assertThrows(IllegalArgumentException.class, () -> atm.upBalance(null, 5));
    }

    @Test
    void testDepositNegativeCount() {
        assertThrows(IllegalArgumentException.class, () -> atm.upBalance(Nominal.ONE_THOUSAND, -3));
    }
}
