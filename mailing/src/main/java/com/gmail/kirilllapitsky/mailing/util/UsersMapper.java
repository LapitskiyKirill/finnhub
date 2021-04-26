package com.gmail.kirilllapitsky.mailing.util;


import com.gmail.kirilllapitsky.mailing.entity.Subscription;
import com.gmail.kirilllapitsky.mailing.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UsersMapper {
    public static List<String> subscriptionToUserEmailMapper(List<Subscription> subscriptions) {
        return subscriptions.stream()
                .map(Subscription::getUser)
                .map(User::getEmail)
                .collect(Collectors.toList());
    }
}

