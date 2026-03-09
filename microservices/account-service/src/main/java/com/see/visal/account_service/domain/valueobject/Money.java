package com.see.visal.account_service.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * Money value object — pairs an amount with a {@link Currency}.
 * Immutable; arithmetic operations return new instances.
 */
public record Money(BigDecimal amount, Currency currency) {

    /** Compact canonical constructor – normalises scale and rejects null. */
    public Money {
        if (amount == null)   throw new IllegalArgumentException("Amount must not be null");
        if (currency == null) throw new IllegalArgumentException("Currency must not be null");
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    // ── Factory helpers ───────────────────────────────────────────────────────

    public static Money ofKHR(BigDecimal amount) { return new Money(amount, Currency.KHR); }
    public static Money ofUSD(BigDecimal amount) { return new Money(amount, Currency.USD); }
    public static Money of(BigDecimal amount, String currencyCode) {
        return new Money(amount, Currency.of(currencyCode));
    }

    // ── Arithmetic ────────────────────────────────────────────────────────────

    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money subtract(Money other) {
        requireSameCurrency(other);
        return new Money(amount.subtract(other.amount), currency);
    }

    public boolean isGreaterThan(Money other) {
        requireSameCurrency(other);
        return amount.compareTo(other.amount) > 0;
    }

    public boolean isGreaterThanOrEqualTo(Money other) {
        requireSameCurrency(other);
        return amount.compareTo(other.amount) >= 0;
    }

    public boolean isNegative() { return amount.compareTo(BigDecimal.ZERO) < 0; }
    public boolean isZero()     { return amount.compareTo(BigDecimal.ZERO) == 0; }

    // ── Internal ──────────────────────────────────────────────────────────────

    private void requireSameCurrency(Money other) {
        if (!currency.code().equals(other.currency.code())) {
            throw new IllegalArgumentException(
                    "Currency mismatch: " + currency.code() + " vs " + other.currency.code());
        }
    }

    @Override
    public String toString() {
        return currency.symbol() + amount.toPlainString() + " " + currency.code();
    }
}
