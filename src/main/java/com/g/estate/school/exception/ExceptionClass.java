package com.g.estate.school.exception;

import java.sql.SQLException;

public class ExceptionClass {

    /**
     * There are two kinds of Exceptions。
     * one is uncheck exception， the other one is check exception
     * uncheck exception is that we need not to check it
     *
     *
     */

    public static void main(String[] args) {

        // uncheck exception
        // parent exception is RuntimeException
        RuntimeException r = new RuntimeException();
        ClassCastException classCastException = new ClassCastException();
        NullPointerException nullPointerException = new NullPointerException();
        ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException = new ArrayIndexOutOfBoundsException();

        StackOverflowError erro = new StackOverflowError();
        OutOfMemoryError error = new OutOfMemoryError(); // this error

        // check exception
        // parent exception is Exception
        // you need try catch or throw it
        SQLException sqlException = new SQLException();
        ClassNotFoundException classNotFoundException = new ClassNotFoundException();
    }
}
