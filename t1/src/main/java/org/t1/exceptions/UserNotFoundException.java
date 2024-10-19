package org.t1.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String nameUser) {
        super("Пользователь с именем " + nameUser + " не был найден в локальном хранилище");
    }
}
