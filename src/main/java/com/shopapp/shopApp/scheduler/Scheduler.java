package com.shopapp.shopApp.scheduler;

import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.ConfirmationToken;
import com.shopapp.shopApp.model.PasswordResetToken;
import com.shopapp.shopApp.model.UserOrder;
import com.shopapp.shopApp.repository.AppUserRepository;
import com.shopapp.shopApp.repository.ConfirmationTokenRepository;
import com.shopapp.shopApp.repository.OrderRepository;
import com.shopapp.shopApp.repository.PasswordResetTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.shopapp.shopApp.constants.TimeConstants.*;

@Slf4j
@Component
@AllArgsConstructor
public class Scheduler {

    private final AppUserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final OrderRepository orderRepository;


    @Scheduled(fixedRate = YEAR_IN_MS * 6)
    public void checkIfUserExpired() {
        List<AppUser> expiredAccounts = userRepository.getExpiredAccounts(LocalDateTime.now());
        if(expiredAccounts.isEmpty()) {
            log.info("No accounts expired");
            return;
        }

        expiredAccounts.forEach(acc -> {
            // if account has expired more than 6 months ago -> delete
            if(acc.getExpiredAt().isBefore(LocalDateTime.now().minusMonths(6))) {
                userRepository.delete(acc);
                log.info(acc.getEmail() + " deleted");
            } else {
//                sendActivationMail(acc);
                log.info("activation mail sent");
            }
        });
    }

    @Scheduled(fixedRate = MONTH_IN_MS)
    public void checkIfConfirmationTokenExpired() {
        List<ConfirmationToken> expiredTokens = confirmationTokenRepository.getExpiredTokens(LocalDateTime.now());
        if(expiredTokens.isEmpty()) {
            log.info("No tokens expired");
            return;
        }

        confirmationTokenRepository.deleteAll(expiredTokens);
        log.info("tokens deleted");
    }

    @Scheduled(fixedRate = MONTH_IN_MS + MINUTE_IN_MS)
    public void checkIfConfirmationTokenConfirmed() {
        List<ConfirmationToken> confirmedTokens = confirmationTokenRepository.getConfirmedTokens();
        if(confirmedTokens.isEmpty()) {
            log.info("No tokens confirmed");
            return;
        }

        confirmationTokenRepository.deleteAll(confirmedTokens);
        log.info("tokens deleted");
    }

    @Scheduled(fixedRate = DAY_IN_MS)
    public void checkIfPasswordResetTokenExpired() {
        List<PasswordResetToken> expiredTokens = passwordResetTokenRepository.getExpiredTokens(LocalDateTime.now());
        if(expiredTokens.isEmpty()) {
            return;
        }

        passwordResetTokenRepository.deleteAll(expiredTokens);
    }

    @Scheduled(fixedRate = DAY_IN_MS + MINUTE_IN_MS)
    public void checkIfPasswordResetTokenUsed() {
        List<PasswordResetToken> usedTokens = passwordResetTokenRepository.getUsedTokens();
        if(usedTokens.isEmpty()) {
            return;
        }

        passwordResetTokenRepository.deleteAll(usedTokens);
    }

    @Scheduled(fixedRate = DAY_IN_MS)
    public void checkIfOrderDelivered() {
        List<UserOrder> deliveredOrders = orderRepository.getDeliveredOrders();
        if(deliveredOrders.isEmpty()) {
            return;
        }

        orderRepository.deleteAll(deliveredOrders);
    }
}
