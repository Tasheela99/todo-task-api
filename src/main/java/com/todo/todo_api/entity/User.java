package com.todo.todo_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Builder
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @Column(length = 80, name = "user_id")
    private String userId;

    @Column(name = "active_state",  columnDefinition = "TINYINT")
    private boolean activeState;

    @Column(length = 250, name = "email")
    private String email;

    @Column(name = "is_account_non_expired",  columnDefinition = "TINYINT")
    private boolean isAccountNonExpired;

    @Column(name = "is_account_non_locked",  columnDefinition = "TINYINT")
    private boolean isAccountNonLocked;

    @Column(name = "is_credentials_non_expired",  columnDefinition = "TINYINT")
    private boolean isCredentialsNonExpired;

    @Column(name = "is_enabled",  columnDefinition = "TINYINT")
    private boolean isEnabled;

    @Column(length = 250, name = "password")
    private String password;

    @Column(length = 100, name = "user_name", unique = true)
    private String username;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private UserRole userRole;

    @OneToMany(mappedBy="user",cascade = CascadeType.ALL)
    private Set<Todo> todos;

}
