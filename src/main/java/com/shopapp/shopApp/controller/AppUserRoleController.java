package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.constants.ResponseConstants;
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

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<?> deleteRoleWithName(@PathVariable String name) throws RoleNotFoundException {
        roleService.deleteRoleWithName(name);
        return ResponseEntity.ok(String.format(ROLE_DELETED, name));
    }

    @PutMapping("/update/{roleName}")
    public ResponseEntity<?> updateRole(@PathVariable String roleName,
                                        @RequestBody @Valid AppUserRoleSaveUpdateDto role) throws RoleNotFoundException {
        roleService.updateRole(roleName, role);
        return ResponseEntity.ok(String.format(ROLE_UPDATED, roleName));
    }
}
