package com.gmail.kirilllapitsky.finnhub.controller;

import com.gmail.kirilllapitsky.finnhub.dto.RegisterRequest;
import com.gmail.kirilllapitsky.finnhub.exception.ApiException;
import com.gmail.kirilllapitsky.finnhub.exception.NoSuchEntityException;
import com.gmail.kirilllapitsky.finnhub.security.enumerable.Role;
import com.gmail.kirilllapitsky.finnhub.service.AdminService;
import com.gmail.kirilllapitsky.finnhub.service.MicroserviceCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/admin")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminController {
    private final AdminService adminService;
    private final MicroserviceCompanyService microserviceCompanyService;

    @PostMapping("/mailing")
    public void mailing(@RequestParam("message") String message) {
        adminService.mailing(message);
    }

    @PostMapping("/fetchAllCompanies")
    public void fetchAllCompaniesFromFetchingService() {
        microserviceCompanyService.fetchAllCompaniesFromFetchingService();
    }

    @PostMapping("/blockUser")
    public void blockUser(@RequestParam("username") String username) throws NoSuchEntityException {
        adminService.blockUser(username);
    }

    @PostMapping("/unBlockUser")
    public void unBlockUser(@RequestParam("username") String username) throws NoSuchEntityException {
        adminService.unBlockUser(username);
    }

    @PostMapping("/createAdminAccount")
    public void createAdminAccount(@RequestBody RegisterRequest registerRequest) throws ApiException {
        adminService.createAdminAccount(registerRequest);
    }

    @PostMapping("/setSubscription")
    public void setSubscription(@RequestParam("username") String username, @RequestParam("Role") Role role) throws NoSuchEntityException {
        adminService.setSubscription(username, role);
    }

    @PostMapping("/deleteSubscription")
    public void deleteSubscription(@RequestParam("username") String username) throws NoSuchEntityException {
        adminService.deleteSubscription(username);
    }

}
