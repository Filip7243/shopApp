package com.shopapp.shopApp.service.confirmationtoken;

import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.ConfirmationToken;

import java.util.List;

public interface ConfirmationTokenService {

    void saveConfirmationToken(ConfirmationToken confirmationToken);
    void deleteConfirmationTokenWithId(Long id);
    List<ConfirmationToken> findByUser(AppUser appUser);
    ConfirmationToken getToken(String token);
    void confirmEmail(ConfirmationToken confirmationToken);

}
