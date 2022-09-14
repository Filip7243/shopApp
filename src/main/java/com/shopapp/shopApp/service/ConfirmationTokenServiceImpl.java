package com.shopapp.shopApp.service;

import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.ConfirmationToken;
import com.shopapp.shopApp.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService{

    private final ConfirmationTokenRepository tokenRepository;

    @Override
    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
        tokenRepository.save(confirmationToken);
    }

    @Override
    public void deleteConfirmationTokenWithId(Long id) {
        tokenRepository.deleteById(id);
    }

    @Override
    public ConfirmationToken findByUser(AppUser appUser) {
        return tokenRepository.findByUser(appUser)
                .orElseThrow(() -> new IllegalStateException("User doesn't exists"));
    }
}
