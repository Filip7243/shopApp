package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.AppUserRoleUpdateDto;
import com.shopapp.shopApp.model.AppUserRole;
import com.shopapp.shopApp.service.AppUserRoleServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/roles")
public class AppUserRoleController {

    private final AppUserRoleServiceImpl roleService;

    @GetMapping("/all")
    public List<AppUserRole> getRoles() {
        return roleService.getRoles();
    }

    @PostMapping("/add")
    public void addRole(@RequestBody AppUserRoleUpdateDto role) {
        AppUserRole newRole = new AppUserRole(null, role.getName(), role.getDescription()); //TODO: mapper
        roleService.saveRole(newRole);
    }

    @DeleteMapping("/delete/{name}")
    public void deleteRoleWithName(@PathVariable String name) {
        roleService.deleteRoleWithName(name);
    }

    @PutMapping("/update/{roleName}")
    public void updateRole(@PathVariable String roleName,
                           @RequestBody AppUserRoleUpdateDto role) {
        roleService.updateRole(roleName, role);
    }

}
