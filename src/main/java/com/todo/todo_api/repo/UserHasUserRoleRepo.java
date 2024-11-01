package com.todo.todo_api.repo;

import com.todo.todo_api.entity.UserRoleHasUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface UserHasUserRoleRepo extends JpaRepository<UserRoleHasUser,String> {

    @Query(value = "SELECT * FROM user_role_has_user WHERE user_id = ?1", nativeQuery = true)
    List<UserRoleHasUser> findByUserId(String userId);
}
