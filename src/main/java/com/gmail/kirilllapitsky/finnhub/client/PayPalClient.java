package com.gmail.kirilllapitsky.finnhub.client;

import com.auth0.jwt.algorithms.Algorithm;
import com.gmail.kirilllapitsky.finnhub.entity.User;
import com.gmail.kirilllapitsky.finnhub.exception.ApiException;
import com.gmail.kirilllapitsky.finnhub.repository.PaymentRepository;
import com.gmail.kirilllapitsky.finnhub.repository.UserRepository;
import com.gmail.kirilllapitsky.finnhub.security.enumerable.Role;
import com.gmail.kirilllapitsky.finnhub.service.SubscriptionService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.auth0.jwt.JWT.require;
import static com.gmail.kirilllapitsky.finnhub.constants.PayPalConstants.*;
import static com.gmail.kirilllapitsky.finnhub.constants.SecurityConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PayPalClient {
    @Value("${paypal.clientId}")
    private String clientId;
    @Value("${paypal.secret}")
    private String clientSecret;
    @Value("${paypal.returnLink}")
    private String returnLink;
    @Value("${paypal.cancelLink}")
    private String cancelLink;
    private final PaymentRepository paymentRepository;
    private final SubscriptionService subscriptionService;
    private final UserRepository userRepository;

    public Map<String, Object> createPayment(HttpServletRequest request, Role role) throws ApiException, PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(CURRENCY);
        amount.setTotal(getPrice(role));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(PAYMENT_METHOD);

        Payment payment = new Payment();
        payment.setIntent(INTENT);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelLink);
        redirectUrls.setReturnUrl(returnLink);
        payment.setRedirectUrls(redirectUrls);

        User user = getUser(request);
        Map<String, Object> response = new HashMap<>();
        Payment createdPayment;

        APIContext context = new APIContext(clientId, clientSecret, PAYMENT_MODE);
        createdPayment = payment.create(context);
        if (createdPayment != null) {
            List<Links> links = createdPayment.getLinks();
            Links redirectLink = links
                    .stream()
                    .filter(link -> link.getRel().equals(APPROVAL_LINK))
                    .findFirst()
                    .get();
            String redirectUrl = redirectLink.getHref();

            com.gmail.kirilllapitsky.finnhub.entity.Payment
                    paymentToSave = new com.gmail.kirilllapitsky.finnhub.entity.Payment();
            paymentToSave.setPaymentId(createdPayment.getId());
            paymentToSave.setUser(user);
            paymentToSave.setIsCompleted(false);
            paymentToSave.setSubscriptionLevel(role);
            paymentRepository.save(paymentToSave);

            response.put(REDIRECT_LINK, redirectUrl);
        }
        return response;
    }

    public void completePayment(HttpServletRequest request) throws ApiException {
        Payment payment = new Payment();
        payment.setId(request.getParameter(REQUEST_PAYMENT_ID));

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(request.getParameter(REQUEST_PAYER_ID));
        try {
            APIContext context = new APIContext(clientId, clientSecret, PAYMENT_MODE);
            Payment createdPayment = payment.execute(context, paymentExecution);
            com.gmail.kirilllapitsky.finnhub.entity.Payment updatedPayment = paymentRepository.findById(createdPayment.getId()).orElseThrow(() -> new ApiException("Invalid transaction."));
            updatedPayment.setIsCompleted(true);
            subscriptionService.setSubscription(updatedPayment.getUser(), updatedPayment.getSubscriptionLevel());
            paymentRepository.save(updatedPayment);
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
    }

    private String getPrice(Role role) throws ApiException {
        switch (role) {
            case BEGINNER:
                return BEGINNER_PRICE;
            case MIDDLE:
                return MIDDLE_PRICE;
            case SENIOR:
                return SENIOR_PRICE;
            default: {
                throw new ApiException("Invalid role passed to payment creation.");
            }
        }
    }

    private User getUser(HttpServletRequest request) throws ApiException {
        String username = require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(request.getHeader(HEADER).replace(TOKEN_PREFIX, ""))
                .getSubject();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("Invalid token."));
    }
}
