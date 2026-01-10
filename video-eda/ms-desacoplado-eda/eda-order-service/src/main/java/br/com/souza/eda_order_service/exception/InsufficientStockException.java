package br.com.souza.eda_order_service.exception;

public class InsufficientStockException extends Exception{

    public InsufficientStockException(String message) {
        super(message);
    }
}
