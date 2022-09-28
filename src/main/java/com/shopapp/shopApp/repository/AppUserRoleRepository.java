package com.shopapp.shopApp.repository;

import com.shopapp.shopApp.model.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRoleRepository extends JpaRepository<AppUserRole, Long> {
    Optional<AppUserRole> findAppUserRoleByName(String name);

    Boolean existsByName(String name);

    boolean existsById(Long id);

}
