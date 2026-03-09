package com.see.visal.account_service.domain.valueobject;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;



/**
 * Currency value object representing supported currencies in the e-banking system.
 * Currently supports KHR (Cambodian Riel) and USD (US Dollar).
 *
 * JSON: deserializes from a plain string  →  "USD"  or  "KHR"
 *       serializes   back to a plain string →  "USD"
 */
public record Currency(String code, String symbol, String displayName) implements Serializable {

    // ── Well-known constants ──────────────────────────────────────────────────
    public static final Currency KHR = new Currency("KHR", "៛", "Cambodian Riel");
    public static final Currency USD = new Currency("USD", "$", "US Dollar");

    // ── Compact canonical constructor ─────────────────────────────────────────
    public Currency {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Currency code must not be null or blank");
        }
        code = code.toUpperCase().strip();
    }

    // ── Jackson: deserialize from plain string "USD" / "KHR" ─────────────────
    @JsonCreator
    public static Currency of(String code) {
        if (code == null) throw new IllegalArgumentException("Currency code must not be null");
        return switch (code.toUpperCase().strip()) {
            case "KHR" -> KHR;
            case "USD" -> USD;
            default    -> new Currency(code.toUpperCase().strip(), "", code.toUpperCase().strip());
        };
    }

    // ── Jackson: serialize to plain string "USD" / "KHR" ─────────────────────
    @JsonValue
    public String getCurrencyCode() { return code; }

    // ── Helpers ───────────────────────────────────────────────────────────────
    public boolean isSupported()  { return "KHR".equals(code) || "USD".equals(code); }
    public boolean isKHR()        { return "KHR".equals(code); }
    public boolean isUSD()        { return "USD".equals(code); }

    @Override
    public String toString() { return code + " (" + displayName + ")"; }
}
