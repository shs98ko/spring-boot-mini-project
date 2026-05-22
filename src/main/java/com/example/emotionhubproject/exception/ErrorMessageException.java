package com.example.emotionhubproject.exception;

public class ErrorMessageException extends RuntimeException{
    public ErrorMessageException(String errorMessage){
        super(errorMessage);
    }
}
