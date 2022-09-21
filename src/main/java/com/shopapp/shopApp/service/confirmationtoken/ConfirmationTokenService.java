package com.shopapp.shopApp.service.confirmationtoken;

import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.ConfirmationToken;

public interface ConfirmationTokenService {

    void saveConfirmationToken(ConfirmationToken confirmationToken);
    void deleteConfirmationTokenWithId(Long id);
    ConfirmationToken findByUser(AppUser appUser);
    ConfirmationToken getToken(String token);
    void confirmEmail(ConfirmationToken confirmationToken);

}
