package com.shopapp.shopApp.service.confirmationtoken;

import com.shopapp.shopApp.exception.token.ConfirmationTokenConfirmedException;
import com.shopapp.shopApp.exception.token.ConfirmationTokenExpiredException;
import com.shopapp.shopApp.exception.token.ConfirmationTokenNotFoundException;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.ConfirmationToken;
import com.shopapp.shopApp.repository.AppUserRepository;
import com.shopapp.shopApp.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.shopapp.shopApp.constants.ExceptionsConstants.*;

@Slf4j
@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

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
    public List<ConfirmationToken> findByUser(AppUser appUser) {
        return tokenRepository.findByUser(appUser);
    }

    @Override
    public ConfirmationToken getToken(String token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new ConfirmationTokenNotFoundException(TOKEN_NOT_FOUND));
    }

    @Override
    public void confirmEmail(ConfirmationToken confirmationToken) {

        if (!tokenRepository.existsByToken(confirmationToken.getToken())) {
            throw new ConfirmationTokenNotFoundException(TOKEN_NOT_FOUND);
        }

        if (confirmationToken.getIsConfirmed()) {
            throw new ConfirmationTokenConfirmedException(String.format(TOKEN_CONFIRMED, confirmationToken.getToken()));
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ConfirmationTokenExpiredException(String.format(TOKEN_EXPIRED, confirmationToken.getExpiresAt().toString()));
        }

        confirmationToken.setIsConfirmed(true);
        AppUser user = confirmationToken.getUser();
        user.setIsEnabled(true);
        tokenRepository.save(confirmationToken);
        userRepository.save(user);
        log.info("EMAIL confirmed!");
    }
}
