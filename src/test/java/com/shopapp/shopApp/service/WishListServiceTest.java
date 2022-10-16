package com.shopapp.shopApp.service;

import com.shopapp.shopApp.exception.product.ProductNotFoundException;
import com.shopapp.shopApp.exception.user.UserCodeNotFoundException;
import com.shopapp.shopApp.exception.user.UserNotFoundException;
import com.shopapp.shopApp.exception.wishlist.WishListNotFoundException;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.Product;
import com.shopapp.shopApp.model.WishList;
import com.shopapp.shopApp.repository.AppUserRepository;
import com.shopapp.shopApp.repository.ProductRepository;
import com.shopapp.shopApp.repository.WishListRepository;
import com.shopapp.shopApp.security.jwt.JwtUtils;
import com.shopapp.shopApp.service.wishlist.WishListServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpRequest;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.shopapp.shopApp.constants.ExceptionsConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WishListServiceTest {

    @Mock
    private WishListRepository wishListRepo;
    @Mock
    private AppUserRepository userRepo;
    @Mock
    private ProductRepository productRepo;

    private JwtUtils jwtUtils;
    private WishListServiceImpl wishListService;

    @BeforeEach
    void setUp() {
        this.jwtUtils = new JwtUtils();
        this.wishListService = new WishListServiceImpl(wishListRepo, userRepo, productRepo, jwtUtils);
    }

    @Test
    void fieldsAreNotNull() {
        assertNotNull(wishListRepo);
        assertNotNull(userRepo);
        assertNotNull(productRepo);
        assertNotNull(jwtUtils);
        assertNotNull(wishListService);
    }

    @Test
    void canCreateWishList() {
        var user = new AppUser();
        var code = anyString();

        when(userRepo.findByUserCode(code)).thenReturn(Optional.of(user));
        String wishListCode = wishListService.createWishList(code);

        assertNotNull(wishListCode);
        assertThat(wishListCode).isInstanceOf(String.class);
    }

    @Test
    void throwsUserCodeNotFoundExceptionWhenCreateWishList() {
        String code = anyString();

        when(userRepo.findByUserCode(code)).thenReturn(Optional.empty());
        UserCodeNotFoundException exception = assertThrows(UserCodeNotFoundException.class,
                () -> wishListService.createWishList(code));

        assertEquals(exception.getMessage(), String.format(USER_CODE_NOT_FOUND, code));
    }

    @Test
    void canDeleteProductFromWishList() {
        var code = anyString();

        var product = new Product();
        product.setProductCode(code);

        var wishList = new WishList();
        wishList.setWishListCode(code);
        wishList.setWishListItems(new HashSet<>());
        wishList.getWishListItems().add(product);
        assertThat(wishList.getWishListItems().size()).isEqualTo(1);

        when(wishListRepo.findByWishListCode(code)).thenReturn(Optional.of(wishList));
        when(productRepo.findByProductCode(code)).thenReturn(Optional.of(product));
        wishListService.deleteProductFromWishList(code, code);

        assertThat(wishList.getWishListItems().size()).isEqualTo(0);
        verify(wishListRepo).save(wishList);
    }

    @Test
    void throwsWishListNotFoundExceptionWhenDeleteProductFromWishList() {
        var code = anyString();

        when(wishListRepo.findByWishListCode(code)).thenReturn(Optional.empty());
        var exception =
                assertThrows(WishListNotFoundException.class, () -> wishListService.deleteProductFromWishList(code, code));

        assertEquals(WISH_LIST_NOT_FOUND, exception.getMessage());
    }

    @Test
    void throwsProductNotFoundExceptionWhenDeleteProductFromWishList() {
        var code = anyString();

        when(wishListRepo.findByWishListCode(code)).thenReturn(Optional.of(new WishList()));
        when(productRepo.findByProductCode(code)).thenReturn(Optional.empty());
        var exception =
                assertThrows(ProductNotFoundException.class, () -> wishListService.deleteProductFromWishList(code, code));

        assertEquals(String.format(PRODUCT_NOT_FOUND, "with code: " + code), exception.getMessage());
    }

    @Test
    void canDeleteWishList() {
        var wishList = new WishList();
        AppUser user = any();

        when(wishListRepo.findByUser(user)).thenReturn(Optional.of(wishList));
        wishListService.deleteWishList(user);

        verify(wishListRepo).delete(wishList);
    }

    @Test
    void throwsUserNotFoundExceptionWhenDeleteWishList() {
        AppUser user = new AppUser();

        when(wishListRepo.findByUser(user)).thenReturn(Optional.empty());
        var exception =
                assertThrows(UserNotFoundException.class, () -> wishListService.deleteWishList(user));

        assertEquals(String.format(USER_NOT_FOUND, user.getEmail()), exception.getMessage());
    }

    @Test
    void canAddProductToWishListWhenWishListExists() {
        var code = anyString();

        var product = new Product();
        var wishList = new WishList();
        wishList.setWishListItems(new HashSet<>());
        Set<Product> wishListItems = wishList.getWishListItems();
        assertEquals(0, wishListItems.size());

        when(wishListRepo.existsByWishListCode(code)).thenReturn(true);
        when(wishListRepo.findByWishListCode(code)).thenReturn(Optional.of(wishList));
        when(productRepo.findByProductCode(code)).thenReturn(Optional.of(product));
        wishListService.addProductToWishList(code, code, null);

        assertEquals(1, wishListItems.size());
        verify(wishListRepo).save(wishList);
    }

}
