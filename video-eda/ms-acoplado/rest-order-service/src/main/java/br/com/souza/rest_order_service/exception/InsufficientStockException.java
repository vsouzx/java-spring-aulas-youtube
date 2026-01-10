package br.com.souza.rest_order_service.exception;

public class InsufficientStockException extends Exception{

    public InsufficientStockException(String message) {
        super(message);
    }
}
