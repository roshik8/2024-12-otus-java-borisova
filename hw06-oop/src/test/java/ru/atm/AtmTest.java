package ru.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AtmTest {
private Atm atm;

    @BeforeEach
    void setUp() {
      atm = new SimpleAtm(Arrays.asList(100, 500, 1000, 5000));
    }

    @Test
    void testDepositAndBalance() {
        atm.upBalance(1000, 5);
        atm.upBalance(500, 10);
        assertEquals(10000, atm.getBalance());
    }

    @Test
    void testWithdrawValidAmount() {
        atm.upBalance(1000, 5);
        atm.upBalance(500, 10);
        Map<Integer, Integer> withdrawn = atm.withdrawMoney(4500);
        assertEquals(Map.of(1000, 4, 500, 1), withdrawn);
        assertEquals(5500, atm.getBalance());
    }

    @Test
    void testWithdrawInvalidAmount() {
        atm.upBalance(1000, 2);
        atm.upBalance(500, 1);
        assertThrows(IllegalArgumentException.class, () -> atm.withdrawMoney(300));
    }

    @Test
    void testWithdrawMoreThanAvailable() {
        atm.upBalance(1000, 2);
        atm.upBalance(500, 1);
        assertThrows(IllegalArgumentException.class, () -> atm.withdrawMoney(5000));
    }

    @Test
    void testDepositInvalidDenomination() {
        assertThrows(IllegalArgumentException.class, () -> atm.upBalance(200, 5));
    }

    @Test
    void testDepositNegativeCount() {
        assertThrows(IllegalArgumentException.class, () -> atm.upBalance(1000, -3));
    }
}
