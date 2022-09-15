package com.shopapp.shopApp.dto;

import com.shopapp.shopApp.model.AppUserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDisplayDto {

    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String userCode;
    private Set<AppUserRole> roles;

}
