package com.shopapp.shopApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String userCode;
    private String name;
    private String lastName;
    private String email; // username
    private String password;
    private String phoneNumber;
    private String address;
    @JsonIgnore
    @ManyToMany(fetch = EAGER)
    @Nullable
    private Set<AppUserRole> roles;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private Boolean isLocked;
    private Boolean isCredentialsExpired;
    private Boolean isEnabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
//        return (LocalDateTime.now().isBefore(this.getExpiredAt()));
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
//        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
//        return !isCredentialsExpired;
    }

    @Override // user is enabled when his email is confirmed
    public boolean isEnabled() {
        return true;
//        return isEnabled;
    }

}
