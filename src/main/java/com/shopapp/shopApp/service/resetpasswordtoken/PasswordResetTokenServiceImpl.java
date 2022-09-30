package com.shopapp.shopApp.service.resetpasswordtoken;

import com.shopapp.shopApp.model.PasswordResetToken;
import com.shopapp.shopApp.repository.PasswordResetTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public void saveToken(PasswordResetToken token) {
        passwordResetTokenRepository.save(token);
    }

    @Override
    public void deleteToken(Long id) {
        PasswordResetToken token = passwordResetTokenRepository.findById(id).orElseThrow();
        passwordResetTokenRepository.delete(token);
    }

    @Override
    public PasswordResetToken getToken(String token) {
        return passwordResetTokenRepository.findByToken(token).orElseThrow();
    }
}
