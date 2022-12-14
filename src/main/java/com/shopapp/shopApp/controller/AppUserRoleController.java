package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.AppUserRoleSaveUpdateDto;
import com.shopapp.shopApp.exception.role.RoleExistsException;
import com.shopapp.shopApp.exception.role.RoleNotFoundException;
import com.shopapp.shopApp.model.AppUserRole;
import com.shopapp.shopApp.service.appuser.AppUserRoleServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.shopapp.shopApp.constants.ResponseConstants.*;
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
    public ResponseEntity<?> addRole(@RequestBody @Valid AppUserRoleSaveUpdateDto role) throws RoleExistsException {
        roleService.saveRole(mapToAppUserRole(role));
        return ResponseEntity.ok(String.format(ROLE_CREATED, role.getName()));
    }

    @DeleteMapping("/delete/{roleName}")
    public ResponseEntity<?> deleteRoleWithName(@PathVariable String roleName) throws RoleNotFoundException {
        roleService.deleteRoleWithName(roleName);
        return ResponseEntity.ok(String.format(ROLE_DELETED, roleName));
    }

    @PutMapping("/update/{roleName}")
    public ResponseEntity<?> updateRole(@PathVariable String roleName,
                                        @RequestBody @Valid AppUserRoleSaveUpdateDto role) throws RoleNotFoundException {
        roleService.updateRole(roleName, role);
        return ResponseEntity.ok(String.format(ROLE_UPDATED, roleName));
    }
}
