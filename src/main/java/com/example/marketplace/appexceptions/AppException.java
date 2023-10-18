package com.example.marketplace.appexceptions;

import java.util.HashMap;
import java.util.Map;

public class AppException extends Exception{
    private String errorName;
    private int status;
    public AppException(String errorName, String message, int status){
        super(message);
        this.errorName = errorName;
        this.status = status;
    }

    public Map<String, String> getDescription(){
        Map<String, String> message = new HashMap<>();
        message.put(errorName, super.getMessage());
        return message;
    }

    public int getStatus() {
        return status;
    }
}
