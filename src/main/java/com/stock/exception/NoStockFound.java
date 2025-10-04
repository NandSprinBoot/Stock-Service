package com.stock.exception;

public class NoStockFound extends RuntimeException{

    public NoStockFound(String message){
        super(message);
    }
}
