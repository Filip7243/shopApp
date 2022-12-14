package com.shopapp.shopApp.repository;

import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    Optional<WishList> findByUser(AppUser user);

    @Query("SELECT w FROM WishList w JOIN FETCH w.wishListItems")
    Optional<WishList> findByWishListCode(String wishListCode);

    Boolean existsByWishListCode(String wishListCode);

}
