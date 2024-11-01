package com.todo.todo_api.service;

import com.todo.todo_api.dto.paginated.PaginatedTodoDto;
import com.todo.todo_api.dto.request.RequestTodoDto;
import com.todo.todo_api.dto.response.ResponseTodoDto;

public interface TodoService {
    void createTodo(String token,RequestTodoDto dto);

    boolean updateTodo(String token,String id,RequestTodoDto dto);

    ResponseTodoDto getTodoById(String token,String id);

    PaginatedTodoDto getAllTodos(String token,int page, int size, String searchText);

    boolean deleteTodo(String token,String id);

    boolean updateTodoState(String token, String id, boolean status);
}
