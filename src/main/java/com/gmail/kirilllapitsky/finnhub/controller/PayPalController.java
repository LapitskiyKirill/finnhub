package com.gmail.kirilllapitsky.finnhub.controller;

import com.gmail.kirilllapitsky.finnhub.client.PayPalClient;
import com.gmail.kirilllapitsky.finnhub.exception.ApiException;
import com.gmail.kirilllapitsky.finnhub.security.enumerable.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/paypal")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PayPalController {
    private final PayPalClient payPalClient;

    @PostMapping(value = "/payment/start")
    public Map<String, Object> makePayment(HttpServletRequest request, @RequestParam("role") Role role) throws ApiException {
        return payPalClient.createPayment(request, role);
    }

    @GetMapping(value = "/payment/complete")
    public void completePayment(HttpServletRequest request) throws ApiException {
        payPalClient.completePayment(request);
    }
}
