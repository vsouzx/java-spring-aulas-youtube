package br.com.souza.monolith_service.exception;

public class InsufficientStockException extends Exception{

    public InsufficientStockException(String message) {
        super(message);
    }
}
