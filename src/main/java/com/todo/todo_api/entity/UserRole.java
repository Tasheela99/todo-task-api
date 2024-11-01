package com.todo.todo_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "user_role")
public class UserRole {
    @Id
    @Column(length = 45, name = "role_id")
    private String roleId;

    @Column(length = 45, name = "role_name", unique = true)
    private String roleName;

    @Column(length = 250, name = "description")
    private String description;

    @Column(name = "active_state", columnDefinition = "TINYINT")
    private boolean activeState;
}
