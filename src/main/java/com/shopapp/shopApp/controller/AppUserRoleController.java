package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.AppUserRoleSaveUpdateDto;
import com.shopapp.shopApp.exception.role.RoleExistsException;
import com.shopapp.shopApp.exception.role.RoleNotFoundException;
import com.shopapp.shopApp.model.AppUserRole;
import com.shopapp.shopApp.service.AppUserRoleServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.shopapp.shopApp.mapper.AppUserRoleMapper.mapToAppUserRole;

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
            roleService.saveRole(mapToAppUserRole(role));
            return ResponseEntity.ok("CREATED");
        } catch (RoleExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<?> deleteRoleWithName(@PathVariable String name) {
        try {
            roleService.deleteRoleWithName(name);
            return ResponseEntity.ok("DELETED");
        } catch (RoleNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{roleName}")
    public ResponseEntity<?> updateRole(@PathVariable String roleName,
                                        @RequestBody AppUserRoleSaveUpdateDto role) {
        try {
            roleService.updateRole(roleName, role);
            return ResponseEntity.ok("UPDATED");
        } catch (RoleNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
