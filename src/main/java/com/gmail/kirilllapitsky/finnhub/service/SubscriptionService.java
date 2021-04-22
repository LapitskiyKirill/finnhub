package com.gmail.kirilllapitsky.finnhub.service;

import com.gmail.kirilllapitsky.finnhub.entity.Subscription;
import com.gmail.kirilllapitsky.finnhub.entity.User;
import com.gmail.kirilllapitsky.finnhub.repository.SubscriptionRepository;
import com.gmail.kirilllapitsky.finnhub.security.enumerable.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    /*if subscription level > new level -> wait for subscription will be ended, then set new subscription
    if subscription level < new level, renew with new level and lose last days of subscription
    if subscription level = new level, renew*/
    public void setSubscription(User user, Role role) {
        Subscription subscription = subscriptionRepository.findById(user.getId()).get();
        if (subscription.getRole().ordinal() > role.ordinal()) {
            subscription.setRenewLevel(role);
            subscription.setShouldBeRenew(true);
        } else if (subscription.getRole().ordinal() < role.ordinal()) {
            subscription.setRole(role);
            subscription.setStartDate(LocalDate.now());
            subscription.setEndDate(LocalDate.now().plusDays(31));
        } else {
            subscription.setRole(role);
            subscription.setEndDate(subscription.getEndDate().plusDays(31));
        }
        subscriptionRepository.save(subscription);
    }


}
