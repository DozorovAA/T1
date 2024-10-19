package org.t1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.t1.config.MainConfig;
import org.t1.exceptions.UserNotFoundException;
import org.t1.services.UserService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
        UserService myService = context.getBean(UserService.class);

        System.out.println(myService.getAllUsers());
        System.out.println();

        try {
            String name = myService.getUserByName("Alex");
            System.out.println("Пользователь " + name + " найден");
        } catch (UserNotFoundException ignore) {

        }
        System.out.println();

        List<String> result = myService.getUsersWhoContainsInLocalUserNames(List.of("Alexey", "Ivan"));
        System.out.println();

        myService.getFirstName();
    }
}