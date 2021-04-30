package com.gmail.kirilllapitsky.finnhub.controller;

import com.gmail.kirilllapitsky.finnhub.dto.RegisterRequest;
import com.gmail.kirilllapitsky.finnhub.exception.ApiException;
import com.gmail.kirilllapitsky.finnhub.exception.NoSuchEntityException;
import com.gmail.kirilllapitsky.finnhub.security.enumerable.Role;
import com.gmail.kirilllapitsky.finnhub.service.AdminService;
import com.gmail.kirilllapitsky.finnhub.service.MicroserviceCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/admin")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminController {
    private final AdminService adminService;
    private final MicroserviceCompanyService microserviceCompanyService;

    @PostMapping("/mailing")
    public ResponseEntity<String> mailing(@RequestParam("message") String message) {
        adminService.mailing(message);
        return new ResponseEntity<>("Successfully mailing.", HttpStatus.OK);
    }

    @PostMapping("/fetchAllCompanies")
    public ResponseEntity<String> fetchAllCompaniesFromFetchingService() {
        microserviceCompanyService.fetchAllCompaniesFromFetchingService();
        return new ResponseEntity<>("Successfully fetched companies.", HttpStatus.OK);
    }

    @PostMapping("/blockUser")
    public ResponseEntity<String> blockUser(@RequestParam("username") String username) throws NoSuchEntityException {
        adminService.blockUser(username);
        return new ResponseEntity<>("User successfully blocked.", HttpStatus.OK);
    }

    @PostMapping("/unBlockUser")
    public ResponseEntity<String> unBlockUser(@RequestParam("username") String username) throws NoSuchEntityException {
        adminService.unBlockUser(username);
        return new ResponseEntity<>("User successfully unblocked.", HttpStatus.OK);
    }

    @PostMapping("/createAdminAccount")
    public ResponseEntity<String> createAdminAccount(@RequestBody RegisterRequest registerRequest) throws ApiException {
        adminService.createAdminAccount(registerRequest);
        return new ResponseEntity<>("Admin account successfully created.", HttpStatus.OK);
    }

    @PostMapping("/setSubscription")
    public ResponseEntity<String> setSubscription(@RequestParam("username") String username, @RequestParam("Role") Role role) throws NoSuchEntityException {
        adminService.setSubscription(username, role);
        return new ResponseEntity<>("User subscription successfully set.", HttpStatus.OK);
    }

    @PostMapping("/deleteSubscription")
    public ResponseEntity<String> deleteSubscription(@RequestParam("username") String username) throws NoSuchEntityException {
        adminService.deleteSubscription(username);
        return new ResponseEntity<>("User subscription successfully deleted.", HttpStatus.OK);
    }
}
