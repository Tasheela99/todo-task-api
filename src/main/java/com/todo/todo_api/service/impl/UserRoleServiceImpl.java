package com.todo.todo_api.service.impl;

import com.todo.todo_api.entity.UserRole;
import com.todo.todo_api.repo.UserRoleRepo;
import com.todo.todo_api.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepo userRoleRepo;


    @Override
    public void initializeUserRoles() {
        List<UserRole> all = userRoleRepo.findAll();
        if (all.isEmpty()) {
            UserRole adminUserRole = new UserRole(
                    UUID.randomUUID().toString(),
                    "ADMIN",
                    "Admin User",
                    true
            );
            UserRole userUser = new UserRole(
                    UUID.randomUUID().toString(),
                    "USER",
                    "User User",
                    true
            );
            userRoleRepo.saveAll(List.of(
                    adminUserRole, userUser));
        }
    }

}
