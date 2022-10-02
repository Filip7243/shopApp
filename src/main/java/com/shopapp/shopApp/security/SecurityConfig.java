package com.shopapp.shopApp.security;

import com.shopapp.shopApp.security.filter.CustomAuthorizationFilter;
import com.shopapp.shopApp.security.handler.CustomLogoutHandler;
import com.shopapp.shopApp.security.jwt.JwtUtils;
import com.shopapp.shopApp.service.appuser.AppUserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.shopapp.shopApp.constants.RoleConstants.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final AppUserServiceImpl userService;
    private final CustomPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final CustomLogoutHandler logoutHandler;

    @Bean
    public CustomAuthorizationFilter authorizationFilter() {
        return new CustomAuthorizationFilter(jwtUtils, userService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder.passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        // requests from /api/users
        http.authorizeRequests().antMatchers("/api/users/accessToken/refresh").permitAll();
        http.authorizeRequests().antMatchers("/api/users/all").hasAnyAuthority(SUPER_ADMIN, ADMIN, MANAGER);
        http.authorizeRequests().antMatchers("/api/users/save").hasAnyAuthority(SUPER_ADMIN, ADMIN);
        http.authorizeRequests().antMatchers("/api/users/delete/{userCode}").hasAuthority(SUPER_ADMIN);
        http.authorizeRequests().antMatchers("/api/users/update/{userCode}").hasAuthority(SUPER_ADMIN);
        http.authorizeRequests().antMatchers("/api/users/roles/add").hasAnyAuthority(SUPER_ADMIN, ADMIN);
        // requests from /api/roles
        http.authorizeRequests().antMatchers("/api/roles/all").hasAnyAuthority(SUPER_ADMIN, ADMIN, MANAGER);
        http.authorizeRequests().antMatchers("/api/roles/add").hasAnyAuthority(SUPER_ADMIN);
        http.authorizeRequests().antMatchers("/api/roles/delete/{roleName}").hasAuthority(SUPER_ADMIN);
        http.authorizeRequests().antMatchers("/api/roles/update/{roleName}").hasAuthority(SUPER_ADMIN);
        // requests from /api/auth
        http.authorizeRequests().antMatchers("/api/auth/signIn").permitAll();
        http.authorizeRequests().antMatchers("/api/auth/signUp").permitAll();
        http.authorizeRequests().antMatchers("/api/auth/confirm").permitAll();
        http.authorizeRequests().antMatchers("/api/auth/password/forget").permitAll();
        http.authorizeRequests().antMatchers("/api/auth/reset/password").permitAll();
        // requests from /api/categories
        http.authorizeRequests().antMatchers("/api/categories/all").hasAnyAuthority(SUPER_ADMIN, ADMIN, MANAGER);
        http.authorizeRequests().antMatchers("/api/categories/add").hasAnyAuthority(SUPER_ADMIN, ADMIN);
        http.authorizeRequests().antMatchers("/api/categories/update/{id}").hasAuthority(SUPER_ADMIN);
        http.authorizeRequests().antMatchers("/api/categories/delete/{id}").hasAuthority(SUPER_ADMIN);
        // requests from /api/orders
        http.authorizeRequests().antMatchers("/api/orders/create").hasAnyAuthority(SUPER_ADMIN, USER);
        http.authorizeRequests().antMatchers("/api/orders/update").hasAuthority(SUPER_ADMIN);
        http.authorizeRequests().antMatchers("/api/orders/delete").hasAuthority(SUPER_ADMIN);
        // requests from /api/products
        http.authorizeRequests().antMatchers("/api/products/all").hasAnyAuthority(SUPER_ADMIN, ADMIN, MANAGER, USER, GUEST);
        http.authorizeRequests().antMatchers("/api/products/add").hasAnyAuthority(SUPER_ADMIN, ADMIN);
        http.authorizeRequests().antMatchers("/api/products/addCategory").hasAnyAuthority(SUPER_ADMIN, ADMIN);
        http.authorizeRequests().antMatchers("/api/products/update/{productCode}").hasAuthority(SUPER_ADMIN);
        http.authorizeRequests().antMatchers("/api/products/delete/{productCode}").hasAuthority(SUPER_ADMIN);
        // requests from /api/product/reviews
        http.authorizeRequests().antMatchers("/api/products/reviews/show").hasAnyAuthority(SUPER_ADMIN, ADMIN, MANAGER);
        http.authorizeRequests().antMatchers("/api/products/reviews/add").hasAnyAuthority(SUPER_ADMIN, ADMIN, MANAGER, USER);
        http.authorizeRequests().antMatchers("/api/products/reviews/update").hasAuthority(SUPER_ADMIN);
        http.authorizeRequests().antMatchers("/api/products/reviews/delete").hasAuthority(SUPER_ADMIN);
        // requests from /api/carts
        http.authorizeRequests().antMatchers("/api/cart/create").hasAuthority(SUPER_ADMIN);
        http.authorizeRequests().antMatchers("/api/cart/items/{shoppingCartCode}").hasAnyAuthority(SUPER_ADMIN, ADMIN, MANAGER, USER);
        http.authorizeRequests().antMatchers("/api/cart/user/add").hasAnyAuthority(SUPER_ADMIN, ADMIN, MANAGER);
        http.authorizeRequests().antMatchers("/api/cart/item/add").hasAnyAuthority(SUPER_ADMIN, ADMIN, MANAGER, USER);
        http.authorizeRequests().antMatchers("/api/cart/item/delete").hasAnyAuthority(SUPER_ADMIN, ADMIN, MANAGER, USER);
        // requests from /api/wishlists
        http.authorizeRequests().antMatchers("/api/wishlist/show").hasAnyAuthority(SUPER_ADMIN, ADMIN, MANAGER, USER);
        http.authorizeRequests().antMatchers("/api/wishlist/create").hasAnyAuthority(SUPER_ADMIN, ADMIN, MANAGER, USER);
        http.authorizeRequests().antMatchers("/api/wishlist/add").hasAnyAuthority(SUPER_ADMIN, ADMIN, MANAGER, USER);
        http.authorizeRequests().antMatchers("/api/wishlist/deleteItem").hasAnyAuthority(SUPER_ADMIN, ADMIN, MANAGER, USER);
        http.authorizeRequests().antMatchers("/api/wishlist/delete").hasAnyAuthority(SUPER_ADMIN, ADMIN, MANAGER, USER);

        http.authorizeRequests().anyRequest().authenticated();

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.logout()
                .logoutSuccessHandler(logoutHandler)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true);

        return http.build();
    }

}
