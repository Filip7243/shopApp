package com.shopapp.shopApp.repository;

import com.shopapp.shopApp.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    // I don't need to use DISTINCT because I store roles in Set
    @Query("SELECT u FROM AppUser u JOIN FETCH u.roles WHERE u.email = :#{#email}")
    Optional<AppUser> findByEmail( String email); // todo; check if n+1 problem exists w get all users
    @Query("SELECT u FROM AppUser u JOIN FETCH u.roles WHERE u.userCode = :#{#userCode}")
    Optional<AppUser> findByUserCode(@Param(value = "userCode") String userCode);

    Boolean existsByEmail(String email);

    @Override
    @Query("SELECT DISTINCT u FROM AppUser u JOIN FETCH u.roles")
    List<AppUser> findAll();

    @Query("SELECT u FROM AppUser u WHERE u.expiredAt < :now")
    List<AppUser> getExpiredAccounts(LocalDateTime now);
}
