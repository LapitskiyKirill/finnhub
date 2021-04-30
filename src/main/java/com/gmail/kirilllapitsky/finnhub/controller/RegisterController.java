package com.gmail.kirilllapitsky.finnhub.controller;

import com.gmail.kirilllapitsky.finnhub.dto.RegisterRequest;
import com.gmail.kirilllapitsky.finnhub.exception.ApiException;
import com.gmail.kirilllapitsky.finnhub.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegisterController {
    private final RegisterService registerService;

    @Transactional
    @PostMapping
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest request) throws ApiException {
        registerService.signUp(request);
        return new ResponseEntity<>("Successfully registered.", HttpStatus.OK);

    }
}
