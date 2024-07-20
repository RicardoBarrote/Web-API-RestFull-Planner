package com.rocketseat.planner.exceptions;

import java.util.UUID;

public class NoSuchElementException extends RuntimeException{

    public NoSuchElementException (UUID id){
        super("Resource not found. Id: " + id);
    }
}
