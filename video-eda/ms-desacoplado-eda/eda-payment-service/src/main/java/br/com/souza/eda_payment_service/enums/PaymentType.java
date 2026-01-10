package br.com.souza.eda_payment_service.enums;

import java.util.Arrays;

public enum PaymentType {
    CREDIT_CARD,
    DEBIT_CARD,
    PIX;

    public static PaymentType fromName(String name) {
        return Arrays.stream(PaymentType.values())
                .filter(paymentType -> paymentType.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Tipo de pagamento inválido: " + name));
    }
}