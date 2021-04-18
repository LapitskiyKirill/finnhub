package com.gmail.kirilllapitsky.finnhub.controller;

import com.gmail.kirilllapitsky.finnhub.dto.RegisterRequest;
import com.gmail.kirilllapitsky.finnhub.exception.ApiException;
import com.gmail.kirilllapitsky.finnhub.service.RegisterService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/register")
@AllArgsConstructor
public class RegisterController {
    private final RegisterService registerService;

    @Transactional
    @PostMapping
    public void signUp(@RequestBody RegisterRequest request) throws ApiException {
        registerService.signUp(request);
    }
}
