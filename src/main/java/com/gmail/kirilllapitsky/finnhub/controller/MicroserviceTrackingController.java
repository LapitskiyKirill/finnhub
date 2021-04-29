package com.gmail.kirilllapitsky.finnhub.controller;

import com.auth0.jwt.algorithms.Algorithm;
import com.gmail.kirilllapitsky.finnhub.dto.CompanyDto;
import com.gmail.kirilllapitsky.finnhub.dto.CompanyMetricsDto;
import com.gmail.kirilllapitsky.finnhub.dto.DailyStockDataDto;
import com.gmail.kirilllapitsky.finnhub.dto.StockDataDto;
import com.gmail.kirilllapitsky.finnhub.entity.User;
import com.gmail.kirilllapitsky.finnhub.exception.ApiException;
import com.gmail.kirilllapitsky.finnhub.exception.NoSuchEntityException;
import com.gmail.kirilllapitsky.finnhub.repository.UserRepository;
import com.gmail.kirilllapitsky.finnhub.service.MicroserviceCompanyService;
import com.gmail.kirilllapitsky.finnhub.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.auth0.jwt.JWT.require;
import static com.gmail.kirilllapitsky.finnhub.constants.SecurityConstants.*;

@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MicroserviceTrackingController {
    private final UserRepository userRepository;
    private final TrackingService trackingService;
    private final MicroserviceCompanyService companyService;

    @PostMapping("/shouldResetTrackingCompanies")
    public Boolean shouldResetTrackingCompanies(@ModelAttribute("user") User user) {
        return trackingService.shouldResetTrackingCompanies(user);
    }

    @PostMapping("/track")
    public void track(@ModelAttribute("user") User user,
                      @RequestParam("displaySymbol") String displaySymbol) throws NoSuchEntityException, ApiException {
        trackingService.track(user, displaySymbol);
    }

    @PostMapping("/unTrack")
    public void unTrack(@ModelAttribute("user") User user,
                        @RequestParam("displaySymbol") String displaySymbol) throws NoSuchEntityException {
        trackingService.unTrack(user, displaySymbol);
    }

    @GetMapping("/getAllCompanies")
    public List<CompanyDto> getAllCompanies(@RequestParam("pageNumber") int pageNumber,
                                            @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return companyService.getAllCompanies(pageable);
    }

    @GetMapping("/getCompanyMetrics")
    public CompanyMetricsDto getCompanyMetrics(@ModelAttribute("user") User user,
                                               @RequestParam("displaySymbol") String displaySymbol) throws ApiException, NoSuchEntityException {
        return companyService.getCompanyMetrics(user, displaySymbol);
    }

    @GetMapping("/getCompanyStockData")
    public List<StockDataDto> getCompanyStockData(@ModelAttribute("user") User user,
                                                  @RequestParam("displaySymbol") String displaySymbol,
                                                  @RequestParam("pageNumber") int pageNumber,
                                                  @RequestParam("pageSize") int pageSize) throws ApiException, NoSuchEntityException {
        return companyService.getCompanyStockData(user, displaySymbol, pageNumber, pageSize);
    }

    @GetMapping("/getCompanyDailyStockData")
    public List<DailyStockDataDto> getCompanyDailyStockData(@ModelAttribute("user") User user,
                                                            @RequestParam("displaySymbol") String displaySymbol,
                                                            @RequestParam("pageNumber") int pageNumber,
                                                            @RequestParam("pageSize") int pageSize) throws ApiException, NoSuchEntityException {
        return companyService.getCompanyDailyStockData(user, displaySymbol, pageNumber, pageSize);
    }

    @GetMapping("/getTrackedCompanies")
    public List<CompanyDto> getTrackedCompanies(@ModelAttribute("user") User user) {
        return companyService.getTrackedCompanies(user);
    }

    @ModelAttribute("user")
    public User getUser(HttpServletRequest request) throws ApiException {
        String username = require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(request.getHeader(HEADER).replace(TOKEN_PREFIX, ""))
                .getSubject();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("Invalid token."));
        if (!user.isAccountNonLocked())
            throw new ApiException("Your account is locked");
        return user;
    }
}
