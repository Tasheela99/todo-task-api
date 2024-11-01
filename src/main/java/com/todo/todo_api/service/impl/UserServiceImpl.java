package com.todo.todo_api.service.impl;

import com.todo.todo_api.dto.request.RequestUserDto;
import com.todo.todo_api.dto.response.ResponseUserDataDto;
import com.todo.todo_api.entity.User;
import com.todo.todo_api.entity.UserRole;
import com.todo.todo_api.entity.UserRoleHasUser;
import com.todo.todo_api.exception.EntryDuplicateException;
import com.todo.todo_api.exception.EntryNotFoundException;
import com.todo.todo_api.jwt.JwtConfig;
import com.todo.todo_api.repo.UserHasUserRoleRepo;
import com.todo.todo_api.repo.UserRepo;
import com.todo.todo_api.repo.UserRoleRepo;
import com.todo.todo_api.service.UserService;
import com.todo.todo_api.util.CommonResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final UserRoleRepo userRoleRepo;
    private final UserHasUserRoleRepo userHasUserRoleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    @Override
    public CommonResponseDto registerUser(RequestUserDto dto) {
        User byEmail = userRepo.findByEmail(dto.getEmail());
        UserRole userRole = userRoleRepo.findUserRoleByRoleName("USER");
        if (byEmail == null) {
            User user = User.builder()
                    .userId(UUID.randomUUID().toString())
                    .activeState(true)
                    .email(dto.getEmail())
                    .isAccountNonExpired(true)
                    .isAccountNonLocked(true)
                    .isCredentialsNonExpired(true)
                    .isEnabled(true)
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .username(dto.getEmail())
                    .userRole(userRole)
                    .build();

            userRepo.save(user);
            UserRoleHasUser userRoleHasUser = new UserRoleHasUser(
                    user,
                    userRole
            );
            userHasUserRoleRepo.save(userRoleHasUser);
            return new CommonResponseDto(
                    201,
                    "USER SAVED SUCCESSFULLY",
                    "USER SAVED",
                    new ArrayList<>()
            );
        }else {
            throw new EntryDuplicateException("USER ALREADY EXISTS");
        }
    }

    public ResponseUserDataDto getAllUserData(String token) {
        String realToken = token.replace(jwtConfig.getTokenPrefix(), "");
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(realToken);
        String username = claimsJws.getBody().getSubject();
        User selectedUser = userRepo.findByUsername(username);
        if (selectedUser == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return new ResponseUserDataDto(
                selectedUser.getUserId(),
                selectedUser.getEmail(),
                selectedUser.getUsername(),
                selectedUser.getUserRole().getRoleName()
        );
    }

    public User getUserByToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return null;
        }
        assert token != null;
        ResponseUserDataDto userData = getAllUserData(token);
        Optional<User> user = userRepo.findById(userData.getUserId());
        if (user.isEmpty()) {
            throw new EntryNotFoundException("User Not Found");
        }
        return user.get();
    }
}
