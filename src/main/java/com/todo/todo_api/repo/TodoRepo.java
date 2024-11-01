package com.todo.todo_api.repo;

import com.todo.todo_api.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TodoRepo extends JpaRepository<Todo, String> {
    @Query(value = "SELECT * FROM todos WHERE (title LIKE %?1% OR priority_type LIKE %?1% OR end_date LIKE %?1%) AND user_id = ?2", nativeQuery = true)
    Page<Todo> findAllTodos(String searchText, Pageable pageable, String userId);

    @Query(value = "SELECT COUNT(*) FROM todos WHERE (title LIKE %?1% OR priority_type LIKE %?1% OR end_date LIKE %?1%) AND user_id = ?2", nativeQuery = true)
    long countAllTodos(String searchText, String userId);
}
