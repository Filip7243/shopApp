package com.shopapp.shopApp.repository;

import com.shopapp.shopApp.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    @Query("SELECT t FROM PasswordResetToken t WHERE t.expiresAt < :now")
    List<PasswordResetToken> getExpiredTokens(LocalDateTime now);

    @Query("SELECT t FROM PasswordResetToken t WHERE t.isPasswordReset = true")
    List<PasswordResetToken> getUsedTokens();
}
