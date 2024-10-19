package org.t1.services;

import org.springframework.stereotype.Service;
import org.t1.exceptions.UserNotFoundException;
import org.t1.annotations.LogAfterReturning;
import org.t1.annotations.LogAfterThrowing;
import org.t1.annotations.LogAround;
import org.t1.annotations.LogBefore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final List<String> localUserNames = new ArrayList<>(List.of("Alexey", "Egor", "Masha", "Sasha"));
    @LogBefore
    public List<String> getAllUsers() {
        return localUserNames;
    }

    @LogAfterThrowing
    public String getUserByName(String name) {
        Optional<String> userName = localUserNames.stream()
                .filter(localUserName -> localUserName.equals(name))
                .findFirst();
        if(userName.isEmpty())
            throw new UserNotFoundException(name);
        return userName.get();
    }

    @LogAfterReturning
    public List<String> getUsersWhoContainsInLocalUserNames(List<String> userNames) {
        return userNames.stream()
                .filter(userNames::contains)
                .collect(Collectors.toList());
    }

    @LogAround
    public Optional<String> getFirstName() {
        return localUserNames.stream().findFirst();
    }
}
