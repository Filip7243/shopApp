package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.AppUserRoleSaveUpdateDto;
import com.shopapp.shopApp.exception.role.RoleExistsException;
import com.shopapp.shopApp.exception.role.RoleNotFoundException;
import com.shopapp.shopApp.model.AppUserRole;
import com.shopapp.shopApp.service.appuser.AppUserRoleServiceImpl;
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
    public ResponseEntity<?> addRole(@RequestBody AppUserRoleSaveUpdateDto role) throws RoleExistsException {
        roleService.saveRole(mapToAppUserRole(role));
        return ResponseEntity.ok("CREATED");
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<?> deleteRoleWithName(@PathVariable String name) throws RoleNotFoundException{
        roleService.deleteRoleWithName(name);
        return ResponseEntity.ok("DELETED");
    }

    @PutMapping("/update/{roleName}")
    public ResponseEntity<?> updateRole(@PathVariable String roleName,
                                        @RequestBody AppUserRoleSaveUpdateDto role) throws RoleNotFoundException {
        roleService.updateRole(roleName, role);
        return ResponseEntity.ok("UPDATED");
    }
}
