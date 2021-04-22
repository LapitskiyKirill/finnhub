package com.gmail.kirilllapitsky.finnhub.service;

import com.gmail.kirilllapitsky.finnhub.entity.Company;
import com.gmail.kirilllapitsky.finnhub.entity.User;
import com.gmail.kirilllapitsky.finnhub.exception.ApiException;
import com.gmail.kirilllapitsky.finnhub.exception.NoSuchEntityException;
import com.gmail.kirilllapitsky.finnhub.repository.CompanyRepository;
import com.gmail.kirilllapitsky.finnhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TrackingService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public Boolean shouldResetTrackingCompanies(User user) {
        return user.getTrackingCompanies().size() > user.getRole().getMaxNumberOfTrackingCompanies();
    }

    public void track(User user, String displaySymbol) throws ApiException, NoSuchEntityException {
        if (user.getTrackingCompanies().size() >= user.getRole().getMaxNumberOfTrackingCompanies())
            throw new ApiException("You can`t track more companies.");
        Company company = getCompanyByDisplaySymbol(displaySymbol);
        user.getTrackingCompanies().add(company);
        userRepository.save(user);
    }

    public void unTrack(User user, String displaySymbol) throws ApiException, NoSuchEntityException {
        Company company = getCompanyByDisplaySymbol(displaySymbol);
        if (user.getTrackingCompanies().contains(company))
            user.getTrackingCompanies().remove(company);
        else
            throw new ApiException(String.format("No company with display symbol %s.", company.getName()));
        userRepository.save(user);
    }

    private Company getCompanyByDisplaySymbol(String displaySymbol) throws NoSuchEntityException {
        return companyRepository.findByDisplaySymbol(displaySymbol)
                .orElseThrow(() -> new NoSuchEntityException(String.format("No company with display symbol %s.", displaySymbol)));
    }
}
