package com.shopapp.shopApp.repository;

import com.shopapp.shopApp.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    @Query("SELECT u FROM AppUser u JOIN FETCH u.roles WHERE u.email = :email")
        // I don't need to use DISTINCT because I store roles in Set
    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByUserCode(String userCode);

    Boolean existsByEmail(String email);

    @Override
    @Query("SELECT DISTINCT u FROM AppUser u JOIN FETCH u.roles")
    List<AppUser> findAll();
}
