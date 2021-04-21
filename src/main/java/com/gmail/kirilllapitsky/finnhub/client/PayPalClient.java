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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.auth0.jwt.JWT.require;
import static com.gmail.kirilllapitsky.finnhub.security.SecurityConstants.*;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PayPalClient {
    @Value("${paypal.clientId}")
    String clientId;
    @Value("${paypal.secret}")
    String clientSecret;
    private final PaymentRepository paymentRepository;
    private final SubscriptionService subscriptionService;
    private final UserRepository userRepository;

    public Map<String, Object> createPayment(HttpServletRequest request, Role role) throws ApiException {
        User user = getUser(request);
        Map<String, Object> response = new HashMap<>();
        Amount amount = new Amount();
        amount.setTotal(String.valueOf(10));
        amount.setCurrency("USD");
        switch (role) {
            case BEGINNER:
                amount.setTotal(String.valueOf(10));
                break;
            case MIDDLE:
                amount.setTotal(String.valueOf(20));
                break;
            case SENIOR:
                amount.setTotal(String.valueOf(30));
                break;
        }
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:4200/cancel");
        redirectUrls.setReturnUrl("http://localhost:8080/api/paypal/payment/complete");
        payment.setRedirectUrls(redirectUrls);
        Payment createdPayment;
        try {
            String redirectUrl = "";
            APIContext context = new APIContext(clientId, clientSecret, "sandbox");
            createdPayment = payment.create(context);
            if (createdPayment != null) {
                List<Links> links = createdPayment.getLinks();
                for (Links link : links) {
                    if (link.getRel().equals("approval_url")) {
                        redirectUrl = link.getHref();
                        break;
                    }
                }
                com.gmail.kirilllapitsky.finnhub.entity.Payment paymentToSave = new com.gmail.kirilllapitsky.finnhub.entity.Payment();
                paymentToSave.setPaymentId(createdPayment.getId());
                paymentToSave.setUser(user);
                paymentToSave.setIsCompleted(false);
                paymentToSave.setSubscriptionLevel(role);
                paymentRepository.save(paymentToSave);
                response.put("status", "success");
                response.put("redirect_url", redirectUrl);
            }
        } catch (PayPalRESTException e) {
            System.out.println("Error happened during payment creation!");
        }
        return response;
    }

    public void completePayment(HttpServletRequest request) throws ApiException {
        Payment payment = new Payment();
        payment.setId(request.getParameter("paymentId"));

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(request.getParameter("PayerID"));
        try {
            APIContext context = new APIContext(clientId, clientSecret, "sandbox");
            Payment createdPayment = payment.execute(context, paymentExecution);
            com.gmail.kirilllapitsky.finnhub.entity.Payment paymentToSave = paymentRepository.findById(createdPayment.getId()).orElseThrow(() -> new ApiException("Invalid transaction"));
            paymentToSave.setIsCompleted(true);
            subscriptionService.setSubscription(paymentToSave.getUser(), paymentToSave.getSubscriptionLevel());
            paymentRepository.save(paymentToSave);
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
        } catch (ApiException e) {
            throw new ApiException(e.getMessage());
        }
    }

    public User getUser(HttpServletRequest request) throws ApiException {
        String username = require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(request.getHeader(HEADER).replace(TOKEN_PREFIX, ""))
                .getSubject();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("Invalid token."));


    }
}
