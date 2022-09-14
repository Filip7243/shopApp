package com.shopapp.shopApp.service;

import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.ConfirmationToken;
import com.shopapp.shopApp.repository.AppUserRepository;
import com.shopapp.shopApp.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Slf4j
@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService{

    private final ConfirmationTokenRepository tokenRepository;
    private final AppUserRepository userRepository;

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

    @Override
    public ConfirmationToken getToken(String token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("There is no token"));
    }

    @Override
    public void confirmEmail(ConfirmationToken confirmationToken) {

        //TODO: it might be deleted
        if(!tokenRepository.existsByToken(confirmationToken.getToken())) {
            throw new IllegalStateException("There is no token at db like this");
        }

        if(confirmationToken.getIsConfirmed()) {
            throw new IllegalStateException("Token already confirmed");
        }

        if(confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Confirmation token expired");
        }

        confirmationToken.setIsConfirmed(true);
        AppUser user = confirmationToken.getUser();
        user.setIsEnabled(true);
        tokenRepository.save(confirmationToken);
        userRepository.save(user);
        log.info("EMAIL confirmed!");
    }
}
