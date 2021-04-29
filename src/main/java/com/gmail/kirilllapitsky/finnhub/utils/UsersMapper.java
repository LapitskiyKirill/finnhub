package com.gmail.kirilllapitsky.finnhub.utils;

import com.gmail.kirilllapitsky.finnhub.entity.Subscription;
import com.gmail.kirilllapitsky.finnhub.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Deprecated
public class UsersMapper {
    public static List<String> subscriptionToUserEmailMapper(List<Subscription> subscriptions) {
        return subscriptions.stream()
                .map(Subscription::getUser)
                .map(User::getEmail)
                .collect(Collectors.toList());
    }
}
