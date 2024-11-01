package com.todo.todo_api.entity;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "user_role_has_user")
public class UserRoleHasUser {

    @EmbeddedId
    private UserRoleHasUserKey id =
            new UserRoleHasUserKey();

    @ManyToOne
    @MapsId("user")
    @JoinColumn(name = "user_id", nullable = false)
    User user;


    @ManyToOne
    @MapsId("userRole")
    @JoinColumn(name = "role_id", nullable = false)
    UserRole userRole;

    public UserRoleHasUser(User user, UserRole userRole) {
        this.user = user;
        this.userRole = userRole;
    }
}
