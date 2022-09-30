package com.shopapp.shopApp.service.resetpasswordtoken;

import com.shopapp.shopApp.model.PasswordResetToken;

public interface PasswordResetTokenService {

    void saveToken(PasswordResetToken token);

    void deleteToken(Long id);

    PasswordResetToken getToken(String token);

}
