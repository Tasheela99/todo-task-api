package com.todo.todo_api.repo;
import com.todo.todo_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface UserRepo extends JpaRepository<User, String> {

    User findByEmail(String username);

    User findByUsername(String username);
}
