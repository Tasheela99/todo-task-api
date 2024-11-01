package com.todo.todo_api.service.impl;

import com.todo.todo_api.auth.ApplicationUser;
import com.todo.todo_api.entity.User;
import com.todo.todo_api.entity.UserRoleHasUser;
import com.todo.todo_api.repo.UserHasUserRoleRepo;
import com.todo.todo_api.repo.UserRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.todo.todo_api.security.ApplicationUserRole.ADMIN;
import static com.todo.todo_api.security.ApplicationUserRole.USER;

@Service
public class ApplicationUserServiceImpl implements UserDetailsService {
    private final UserRepo userRepo;
    private final UserHasUserRoleRepo userHasUserRoleRepo;

    public ApplicationUserServiceImpl(UserRepo userRepo, UserHasUserRoleRepo userHasUserRoleRepo) {
        this.userRepo = userRepo;
        this.userHasUserRoleRepo = userHasUserRoleRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User byUsername = userRepo.findByEmail(username);
        if (byUsername == null) {
            throw new UsernameNotFoundException(
                    String.format("username %s not found", username));
        }
        List<UserRoleHasUser> userRole = userHasUserRoleRepo.
                findByUserId(byUsername.getUserId());
        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();

        for (UserRoleHasUser user :userRole){
            if (user.getUserRole().getRoleName().equals("USER")){
                grantedAuthorities.addAll(USER.getGrantedAuthorities());
            }
            if (user.getUserRole().getRoleName().equals("ADMIN")){
                grantedAuthorities.addAll(ADMIN.getGrantedAuthorities());
            }

        }
        ApplicationUser user = null;
                user = new ApplicationUser(
                    byUsername.getPassword(),
                    byUsername.getEmail(),
                    grantedAuthorities,
                    byUsername.isAccountNonExpired(),
                    byUsername.isAccountNonLocked(),
                    true,
                    true
            );
        return user;
    }
}
