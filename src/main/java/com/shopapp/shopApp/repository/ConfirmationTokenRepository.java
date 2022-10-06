package com.shopapp.shopApp.repository;

import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    List<ConfirmationToken> findByUser(AppUser appUser);

    Optional<ConfirmationToken> findByToken(String token);

    Boolean existsByToken(String token);

    @Query("SELECT c FROM ConfirmationToken c WHERE c.expiresAt < :now")
    List<ConfirmationToken> getExpiredTokens(LocalDateTime now);

    @Query("SELECT c FROM ConfirmationToken c WHERE c.isConfirmed = true")
    List<ConfirmationToken> getConfirmedTokens();
}
