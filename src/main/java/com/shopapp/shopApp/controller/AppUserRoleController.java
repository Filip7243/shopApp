package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.AppUserRoleSaveUpdateDto;
import com.shopapp.shopApp.model.AppUserRole;
import com.shopapp.shopApp.service.AppUserRoleServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/roles")
public class AppUserRoleController {

    private final AppUserRoleServiceImpl roleService;

    @GetMapping("/all")
    public ResponseEntity<List<AppUserRole>> getRoles() {
        return ResponseEntity.ok(roleService.getRoles());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRole(@RequestBody AppUserRoleSaveUpdateDto role) {
        try {
            AppUserRole newRole = new AppUserRole(null, role.getName(), role.getDescription()); //TODO: mapper
            roleService.saveRole(newRole);
            return ResponseEntity.ok("CREATED");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<?> deleteRoleWithName(@PathVariable String name) {
        try {
            roleService.deleteRoleWithName(name);
            return ResponseEntity.ok("DELETED");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{roleName}")
    public ResponseEntity<?> updateRole(@PathVariable String roleName,
                                        @RequestBody AppUserRoleSaveUpdateDto role) {
        try {
            roleService.updateRole(roleName, role);
            return ResponseEntity.ok("UPDATED");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
